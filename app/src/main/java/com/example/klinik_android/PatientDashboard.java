package com.example.klinik_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientDashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView usernameTextView;
    private Button btnViewAppointments, btnOrderMedicine, btnChatbot, btnViewMedicalRecords;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find the TextView for displaying the username
        usernameTextView = findViewById(R.id.profile_name);

        // Fetch and set the username from Firestore (use code provided earlier)

        // Find the buttons
        btnViewAppointments = findViewById(R.id.btn_view_appointments);
        btnOrderMedicine = findViewById(R.id.btn_order_medicine);
        btnChatbot = findViewById(R.id.btn_chatbot);
        btnViewMedicalRecords = findViewById(R.id.btn_view_medical_records);

        // Set onClick listeners for each button
        btnViewAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboard.this, ViewAppointmentsActivity.class);
            startActivity(intent);
        });

        btnOrderMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboard.this, OrderMedicineActivity.class);
            startActivity(intent);
        });

        btnViewMedicalRecords.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboard.this, ViewMedicalRecordsActivity.class);
            startActivity(intent);
        });
    }
}
