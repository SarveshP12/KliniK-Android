package com.example.klinik_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etUsername;
    private Button btnRegister;
    private RadioGroup userTypeRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etUsername = findViewById(R.id.et_name);  // Added username input
        btnRegister = findViewById(R.id.btn_register);
        userTypeRadioGroup = findViewById(R.id.user_type_radio_group); // Initialize RadioGroup

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Register button click listener
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String username = etUsername.getText().toString().trim();  // Capture the username

        // Ensure all fields are filled
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if password is at least 6 characters long
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters long");
            etPassword.requestFocus();
            return;
        }

        // Get selected user type
        int selectedUserTypeId = userTypeRadioGroup.getCheckedRadioButtonId();
        if (selectedUserTypeId == -1) {
            Toast.makeText(this, "Please select a user type!", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedUserType = findViewById(selectedUserTypeId);
        String userType = selectedUserType.getText().toString(); // Get "Doctor" or "Patient"

        // Create a new user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the newly created user
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();  // Get the user ID

                            // Prepare data to store in Firestore
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);  // Store username
                            user.put("email", email);        // Store email
                            user.put("userType", userType);   // Store user type

                            // Save user data to Firestore using the user ID as the document key
                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        // Show success message
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                                        // Navigate to the appropriate dashboard based on user type
                                        Intent intent;
                                        if (userType.equals("Doctor")) {
                                            intent = new Intent(RegisterActivity.this, DoctorDashboard.class);
                                        } else {
                                            intent = new Intent(RegisterActivity.this, PatientDashboard.class);
                                        }
                                        intent.putExtra("USERNAME", username);  // Optionally pass the username
                                        startActivity(intent);
                                        finish();  // Finish the RegisterActivity
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle Firestore failure case
                                        Toast.makeText(RegisterActivity.this, "Error storing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Handle registration failure case
                        String errorMessage = "Registration failed.";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            errorMessage = "Weak password: " + e.getReason();
                        } catch (FirebaseAuthUserCollisionException e) {
                            errorMessage = "An account already exists with this email.";
                        } catch (Exception e) {
                            errorMessage = e.getMessage();
                        }
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
