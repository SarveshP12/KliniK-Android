package com.example.klinik_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private EditText etSpecialization, etAddress, etPhoneNo, etTextarea, etPhysID;
    private Button btnSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        etSpecialization = findViewById(R.id.et_specialization);
        etAddress = findViewById(R.id.et_address);
        etPhoneNo = findViewById(R.id.et_phone_no);
        etTextarea = findViewById(R.id.et_textarea);
        etPhysID = findViewById(R.id.et_phys_id);
        btnSave = findViewById(R.id.btn_save);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Save button click listener
        btnSave.setOnClickListener(v -> saveDoctorDetails());
    }

    private void saveDoctorDetails() {
        String specialization = etSpecialization.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phoneNo = etPhoneNo.getText().toString().trim();
        String description = etTextarea.getText().toString().trim();
        String physID = etPhysID.getText().toString().trim();

        // Ensure required fields are filled
        if (specialization.isEmpty() || address.isEmpty() || phoneNo.isEmpty() || physID.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Prepare data to save
        Map<String, Object> doctorData = new HashMap<>();
        doctorData.put("specialization", specialization);
        doctorData.put("address", address);
        doctorData.put("phoneNo", phoneNo);
        doctorData.put("description", description);  // Optional field
        doctorData.put("physID", physID);

        // Save data to Firestore under "doctors" collection with document ID as user ID
        db.collection("doctors").document(userId)
                .update(doctorData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SettingsActivity.this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the settings activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SettingsActivity.this, "Error saving details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
