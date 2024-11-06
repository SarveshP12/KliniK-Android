package com.example.klinik_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private List<Doctor> doctorList;
    private Context context;

    public DoctorAdapter(List<Doctor> doctorList, Context context) {
        this.doctorList = doctorList;
        this.context = context;
    }

    public void updateList(List<Doctor> newDoctorList) {
        doctorList = new ArrayList<>(newDoctorList); // Create a new list to avoid modifying the original list reference
        notifyDataSetChanged(); // Notify RecyclerView to refresh with the new data
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.nameTextView.setText(doctor.getUsername());
        holder.specialtyTextView.setText(doctor.getSpecialization());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingAppointment.class);
            intent.putExtra("physID", doctor.getPhysID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, specialtyTextView;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            specialtyTextView = itemView.findViewById(R.id.specialtyTextView);
        }
    }
}
