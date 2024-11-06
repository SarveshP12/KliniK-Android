package com.example.klinik_android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookingAppointment extends AppCompatActivity {
    private TextView physidTextView,nameTextView, specialtyTextView, addressTextView, phoneTextView, descriptionTextView;
    private String physID;

    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);

        // Initialize the views
        physidTextView = findViewById(R.id.physidTextView);
        nameTextView = findViewById(R.id.doctorNameTextView);
        specialtyTextView = findViewById(R.id.specialtyTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        // Get the physID passed from the DoctorAdapter
        physID = getIntent().getStringExtra("physID");

        db = FirebaseFirestore.getInstance();

        if (physID != null) {
            loadDoctorDetails();
        } else {
            Toast.makeText(this, "No Doctor ID provided", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no physID was provided
        }
    }

    private void loadDoctorDetails() {
        db.collection("doctors").whereEqualTo("physID", physID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Assuming only one document will match
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        // Extract fields from Firestore document
                        String physID = documentSnapshot.getString("physID");
                        String username = documentSnapshot.getString("username");
                        String specialization = documentSnapshot.getString("specialization");
                        String address = documentSnapshot.getString("address");
                        String phoneNo = documentSnapshot.getString("phoneNo");
                        String description = documentSnapshot.getString("description");

                        // Set the data to TextViews
                        physidTextView.setText(physID != null ? physID : "N/A");
                        nameTextView.setText(username != null ? username : "N/A");
                        specialtyTextView.setText(specialization != null ? specialization : "N/A");
                        addressTextView.setText(address != null ? address : "N/A");
                        phoneTextView.setText(phoneNo != null ? phoneNo : "N/A");
                        descriptionTextView.setText(description != null ? description : "N/A");
                    } else {
                        Toast.makeText(this, "Doctor details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
