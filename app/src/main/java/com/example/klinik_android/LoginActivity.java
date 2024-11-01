package com.example.klinik_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private View btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Firebase login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful, get the current user
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // First, check if the user is a doctor
                            db.collection("doctors").document(userId).get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && task1.getResult().exists()) {
                                            // User is a doctor
                                            Toast.makeText(LoginActivity.this, "Doctor user found", Toast.LENGTH_SHORT).show();
                                            navigateToDashboard("Doctor");
                                        } else {
                                            // If not a doctor, check if the user is a patient
                                            db.collection("patients").document(userId).get()
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful() && task2.getResult().exists()) {
                                                            // User is a patient
                                                            navigateToDashboard("Patient");
                                                        } else {
                                                            // If user data is not found in both collections
                                                            Toast.makeText(LoginActivity.this, "User type not found.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });
                        }
                    } else {
                        // Handle login errors
                        String errorMessage = "Login failed.";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            errorMessage = "No account with this email found.";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            errorMessage = "Invalid password.";
                        } catch (Exception e) {
                            errorMessage = "Login failed: " + e.getMessage();
                        }

                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void navigateToDashboard(String userType) {
        Intent intent;
        if ("Doctor".equals(userType)) {
            Toast.makeText(this, "taking to doctors dashboard", Toast.LENGTH_SHORT).show();
            intent = new Intent(LoginActivity.this, DoctorDashboard.class);
        } else {
            intent = new Intent(LoginActivity.this, PatientDashboard.class);
        }
        startActivity(intent);
        finish(); // Close the LoginActivity so the user can't go back to it
    }
}
