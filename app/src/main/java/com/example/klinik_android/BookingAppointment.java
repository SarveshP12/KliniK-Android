package com.example.klinik_android;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingAppointment extends AppCompatActivity {
    private ImageView doctorPhotoImageView;
    private TextView physidTextView, nameTextView, specialtyTextView, addressTextView, phoneTextView, descriptionTextView;
    private TextView patientName;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button bookAppointmentButton;
    private String physID, doctorName, photoUrl;
    private boolean status = false;

    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);

        // Initialize the views
        doctorPhotoImageView = findViewById(R.id.doctorPhotoImageView);
        physidTextView = findViewById(R.id.physidTextView);
        nameTextView = findViewById(R.id.doctorNameTextView);
        specialtyTextView = findViewById(R.id.specialtyTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        datePicker = findViewById(R.id.calendarView); // Assuming DatePicker is used for date selection
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);
        timePicker = findViewById(R.id.timePicker);

        patientName = findViewById(R.id.profile_name);
        // Get the physID passed from the DoctorAdapter
        physID = getIntent().getStringExtra("physID");

        db = FirebaseFirestore.getInstance();

        if (physID != null) {
            loadDoctorDetails();
        } else {
            Toast.makeText(this, "No Doctor ID provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up button click to book appointment
        bookAppointmentButton.setOnClickListener(v -> bookAppointment());
    }

    private void loadDoctorDetails() {
        db.collection("doctors").whereEqualTo("physID", physID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        // Extract fields from Firestore document
                        photoUrl = documentSnapshot.getString("photoUrl");
                        doctorName = documentSnapshot.getString("username");
                        String specialization = documentSnapshot.getString("specialization");
                        String address = documentSnapshot.getString("address");
                        String phoneNo = documentSnapshot.getString("phoneNo");
                        String description = documentSnapshot.getString("description");

                        // Set the data to TextViews
                        physidTextView.setText(physID != null ? physID : "N/A");
                        nameTextView.setText(doctorName != null ? doctorName : "N/A");
                        specialtyTextView.setText(specialization != null ? specialization : "N/A");
                        addressTextView.setText(address != null ? address : "N/A");
                        phoneTextView.setText(phoneNo != null ? phoneNo : "N/A");
                        descriptionTextView.setText(description != null ? description : "N/A");

                        // Load doctor photo using Glide
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(photoUrl)
                                    .placeholder(R.drawable.circle_background) // A placeholder image
                                    .into(doctorPhotoImageView);
                        } else {
                            doctorPhotoImageView.setImageResource(R.drawable.circle_background);
                        }
                    } else {
                        Toast.makeText(this, "Doctor details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void bookAppointment() {
        // Retrieve date from DatePicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        // Retrieve time from TimePicker
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Set date and time in Calendar instance
        Calendar appointmentDateTime = Calendar.getInstance();
        appointmentDateTime.set(year, month, day, hour, minute);

        // Prepare data for Firestore
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("doctorName", doctorName);
        appointmentData.put("patientName", patientName);
        appointmentData.put("appointmentDateTime", appointmentDateTime);
        appointmentData.put("physID", physID);
        appointmentData.put("status",status);

        // Store the appointment data in Firestore under "appointments" collection
        db.collection("appointments").add(appointmentData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to book appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
