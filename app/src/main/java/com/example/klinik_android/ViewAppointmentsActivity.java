package com.example.klinik_android;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAppointmentsActivity extends AppCompatActivity {

    private RecyclerView appointmentsRecyclerView;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointmentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        appointmentsRecyclerView = findViewById(R.id.appointmentsRecyclerView);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        appointmentsRecyclerView.setAdapter(appointmentAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch appointments from Firestore
        fetchAppointmentsFromFirestore();
    }

    private void fetchAppointmentsFromFirestore() {
        CollectionReference appointmentsRef = db.collection("appointments");

        appointmentsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    appointmentList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Appointment appointment = documentSnapshot.toObject(Appointment.class);
                        appointmentList.add(appointment);
                    }
                    appointmentAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(ViewAppointmentsActivity.this, "Failed to load appointments.", Toast.LENGTH_SHORT).show());
    }
}
