package com.example.klinik_android;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
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

public class OrderMedicine extends AppCompatActivity {
    private RecyclerView medicineRecyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;
    private EditText searchEditText;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_medicine);

        searchEditText = findViewById(R.id.searchEditText);
        medicineRecyclerView = findViewById(R.id.medicineRecyclerView);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        medicineList = new ArrayList<>();
        medicineAdapter = new MedicineAdapter(this, medicineList);
        medicineRecyclerView.setAdapter(medicineAdapter);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("medicine");

        // Fetch data from Firebase Realtime Database
        fetchDataFromDatabase();

        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void fetchDataFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineList.clear();
                for (DataSnapshot medicineSnapshot : snapshot.getChildren()) {
                    Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                    if (medicine != null) {
                        medicineList.add(medicine);
                    }
                }
                medicineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderMedicine.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("OrderMedicine", "Database error: " + error.getMessage());
            }
        });
    }

    private void filter(String text) {
        List<Medicine> filteredList = new ArrayList<>();
        for (Medicine item : medicineList) {
            if (item.getMedicine_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        medicineAdapter = new MedicineAdapter(this, filteredList);
        medicineRecyclerView.setAdapter(medicineAdapter);
    }
}
