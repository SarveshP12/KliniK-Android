package com.example.klinik_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddToCart extends AppCompatActivity {

    private TextView medicineNameTextView, totalAmountTextView;
    private Button addMoreItemsButton, buyMedicineButton;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        // Initialize views
        medicineNameTextView = findViewById(R.id.medicineNameTextView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        addMoreItemsButton = findViewById(R.id.addMoreItemsButton);
        buyMedicineButton = findViewById(R.id.buyMedicineButton);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get data from the intent
        String medicineName = getIntent().getStringExtra("medicineName");
        double price = getIntent().getDoubleExtra("price", 0.0);

        // Display the data
        medicineNameTextView.setText("Medicine: " + medicineName);
        totalAmountTextView.setText("Total Amount: ₹" + price);

        // "Add More Items" button logic
        addMoreItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddToCart.this, OrderMedicine.class);
            startActivity(intent);
        });

        // "Buy Medicine" button logic
        buyMedicineButton.setOnClickListener(v -> {
            saveOrderToFirestore(medicineName, price);
        });
    }

    private void saveOrderToFirestore(String medicineName, double totalAmount) {
        // Reference to the "orders" collection in the "patient" collection in Firestore
        CollectionReference ordersRef = db.collection("patients").document("your_patient_id").collection("orders");

        // Create a map to hold order details
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("medicine_name", medicineName);
        orderData.put("total_amount", totalAmount);

        // Add order to Firestore
        ordersRef.add(orderData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddToCart.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate to another screen or finish the activity here
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddToCart.this, "Failed to place order. Try again.", Toast.LENGTH_SHORT).show();
                });
    }
}
