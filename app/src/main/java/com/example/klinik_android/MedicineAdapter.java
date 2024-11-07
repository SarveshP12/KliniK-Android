package com.example.klinik_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.medicineNameTextView.setText(medicine.getMedicine_name());
        holder.expiryDateTextView.setText("Expiry Date: " + medicine.getExpiry_date());
        holder.priceTextView.setText("Price: ₹" + medicine.getPrice());
        holder.quantityTextView.setText("Quantity: " + medicine.getQuantity());

        holder.addToCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddToCart.class);
            intent.putExtra("medicineName", medicine.getMedicine_name());
            intent.putExtra("price", medicine.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView medicineNameTextView, expiryDateTextView, priceTextView, quantityTextView;
        Button addToCartButton;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineNameTextView = itemView.findViewById(R.id.medicineNameTextView);
            expiryDateTextView = itemView.findViewById(R.id.expiryDateTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
