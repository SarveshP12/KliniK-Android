package com.example.klinik_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

// Other imports remain the same

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private View btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "LoginActivity"; // Tag for logging

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
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            checkUserType(email);
                        }
                    } else {
                        handleLoginError(task);
                    }
                });
    }

    private void checkUserType(String email) {
        Log.d(TAG, "Checking if user is a doctor...");
        // First, check the "doctors" collection for the email
        db.collection("doctors").whereEqualTo("email", email).get()
                .addOnCompleteListener(doctorTask -> {
                    if (doctorTask.isSuccessful()) {
                        if (!doctorTask.getResult().isEmpty()) {
                            Log.d(TAG, "User found in doctors collection.");
                            navigateToDashboard("Doctor");
                        } else {
                            Log.d(TAG, "User not found in doctors collection, checking patients collection...");
                            // Check in "patients" collection if not found in "doctors"
                            db.collection("patients").whereEqualTo("email", email).get()
                                    .addOnCompleteListener(patientTask -> {
                                        if (patientTask.isSuccessful() && !patientTask.getResult().isEmpty()) {
                                            Log.d(TAG, "User found in patients collection.");
                                            navigateToDashboard("Patient");
                                        } else {
                                            Log.d(TAG, "User not found in both doctors and patients collections.");
                                            Toast.makeText(LoginActivity.this, "User type not found.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Log.e(TAG, "Error checking doctors collection: ", doctorTask.getException());
                        Toast.makeText(LoginActivity.this, "Error retrieving user type.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToDashboard(String userType) {
        Intent intent;
        if ("Doctor".equals(userType)) {
            Log.d(TAG, "Navigating to Doctor's Dashboard.");
            Toast.makeText(this, "Taking to Doctor's Dashboard", Toast.LENGTH_SHORT).show();
            intent = new Intent(LoginActivity.this, DoctorDashboard.class);
        } else {
            Log.d(TAG, "Navigating to Patient's Dashboard.");
            Toast.makeText(this, "Taking to Patient's Dashboard", Toast.LENGTH_SHORT).show();
            intent = new Intent(LoginActivity.this, PatientDashboard.class);
        }
        startActivity(intent);
        finish();
    }

    private void handleLoginError(Task<AuthResult> task) {
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
}
