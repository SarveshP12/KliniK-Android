package com.example.klinik_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private Context context;

    public MedicineAdapter(List<Medicine> medicineList, Context context) {
        this.medicineList = medicineList;
        this.context = context;
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
        holder.medicineNameTextView.setText(medicine.getMedicineName());
        holder.expiryDateTextView.setText("Expiry Date: " + medicine.getExpiryDate());
        holder.priceTextView.setText("Price: ₹" + medicine.getPrice());
        holder.quantityTextView.setText("Quantity: " + medicine.getQuantity());
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView medicineNameTextView, expiryDateTextView, priceTextView, quantityTextView;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineNameTextView = itemView.findViewById(R.id.medicineNameTextView);
            expiryDateTextView = itemView.findViewById(R.id.expiryDateTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }
    }
}
