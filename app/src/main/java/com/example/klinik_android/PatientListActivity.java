package com.example.klinik_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientListActivity extends AppCompatActivity {

    private static final String TAG = "PatientListActivity";
    private FirebaseFirestore firestore;
    private ListView patientListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> patientNames = new ArrayList<>();
    private HashMap<String, String> patientIdMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        firestore = FirebaseFirestore.getInstance();
        patientListView = findViewById(R.id.patient_list_view);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patientNames);
        patientListView.setAdapter(adapter);

        loadPatientList();

        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPatientName = patientNames.get(position);
                String patientId = patientIdMap.get(selectedPatientName);

                if (patientId != null) {
                    Intent intent = new Intent(PatientListActivity.this, ViewMedicalRecordsActivity.class);
                    intent.putExtra("patientId", patientId);
                    startActivity(intent);
                } else {
                    Toast.makeText(PatientListActivity.this, "Patient ID not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPatientList() {
        firestore.collection("Patients").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String patientId = document.getId();  // Assuming document ID is patientId

                            if (name != null && patientId != null) {
                                patientNames.add(name);
                                patientIdMap.put(name, patientId);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}
