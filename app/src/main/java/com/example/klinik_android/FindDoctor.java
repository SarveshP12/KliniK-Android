package com.example.klinik_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindDoctor extends AppCompatActivity {

    private RecyclerView doctorRecyclerView;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;
    private DatabaseReference databaseReference;

    private Button dentalSpecialistButton;
    private Button heartSpecialistButton;
    private Button childSpecialistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        // Initialize RecyclerView and doctor list
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(this,doctorList); // Pass context and doctor list
        doctorRecyclerView.setAdapter(doctorAdapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors");

        // Initialize filter buttons
        dentalSpecialistButton = findViewById(R.id.dental_specialist_button);
        heartSpecialistButton = findViewById(R.id.heart_specialist_button);
        childSpecialistButton = findViewById(R.id.medicine_button);

        // Load all doctors from Firebase
        fetchDoctorsFromFirebase();

        // Set up filter button click listeners
        dentalSpecialistButton.setOnClickListener(v -> filterDoctors("Dental Specialist"));
        heartSpecialistButton.setOnClickListener(v -> filterDoctors("Heart Specialist"));
        childSpecialistButton.setOnClickListener(v -> filterDoctors("Child Specialist"));
    }

    private void fetchDoctorsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctorList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        doctorList.add(doctor); // physID will be included automatically if present in Firebase
                    }
                }
                doctorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FindDoctor.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterDoctors(String specialty) {
        List<Doctor> filteredList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getSpecialty().equals(specialty)) {
                filteredList.add(doctor);
            }
        }

        // Create new adapter with filtered data and set it to RecyclerView
        doctorAdapter = new DoctorAdapter(this, filteredList); // Pass the filtered list
        doctorRecyclerView.setAdapter(doctorAdapter);

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No doctors found for " + specialty, Toast.LENGTH_SHORT).show();
        }
    }
}
