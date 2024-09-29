package com.example.klinik_android;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientDashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find the TextView for displaying the username
        usernameTextView = findViewById(R.id.profile_name);

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Get the user ID
            String userId = currentUser.getUid();

            // Fetch the user data from Firestore
            fetchUsernameFromFirestore(userId);
        } else {
            // If no user is logged in, redirect to LoginActivity or handle it accordingly
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUsernameFromFirestore(String userId) {
        // Retrieve the user document from Firestore
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Get the username from Firestore document
                            String username = document.getString("username");

                            // Set the username in the TextView
                            if (username != null) {
                                usernameTextView.setText("Welcome, " + username);
                            }
                        }
                    } else {
                        Toast.makeText(PatientDashboard.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
