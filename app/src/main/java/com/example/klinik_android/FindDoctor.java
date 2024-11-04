package com.example.klinik_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FindDoctor extends AppCompatActivity {

    private RecyclerView doctorRecyclerView;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;
    private FirebaseFirestore firestore;
    private CollectionReference doctorsRef;

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
        doctorAdapter = new DoctorAdapter(doctorList, this);
        doctorRecyclerView.setAdapter(doctorAdapter);

        // Initialize Firestore reference
        firestore = FirebaseFirestore.getInstance();
        doctorsRef = firestore.collection("doctors");

        // Initialize filter buttons
        dentalSpecialistButton = findViewById(R.id.dental_specialist_button);
        heartSpecialistButton = findViewById(R.id.heart_specialist_button);
        childSpecialistButton = findViewById(R.id.medicine_button);

        // Load all doctors from Firestore
        fetchDoctorsFromFirestore();

        // Set up filter button click listeners
        dentalSpecialistButton.setOnClickListener(v -> filterDoctors("Dental Specialist"));
        heartSpecialistButton.setOnClickListener(v -> filterDoctors("Heart Specialist"));
        childSpecialistButton.setOnClickListener(v -> filterDoctors("Child Specialist"));
    }

    private void fetchDoctorsFromFirestore() {
        // Fetching all doctors only once
        doctorsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    doctorList.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Doctor doctor = document.toObject(Doctor.class);
                        if (doctor != null) {
                            doctorList.add(doctor);
                        }
                    }
                    doctorAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(FindDoctor.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterDoctors(String specialization) {
        List<Doctor> filteredList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getSpecialization().equals(specialization)) {
                filteredList.add(doctor);
            }
        }

        // Update the adapter with the filtered list
        doctorAdapter.updateList(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No doctors found for " + specialization, Toast.LENGTH_SHORT).show();
        }
    }
}
