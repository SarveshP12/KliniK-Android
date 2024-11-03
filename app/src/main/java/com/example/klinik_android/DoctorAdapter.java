package com.example.klinik_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private Context context;
    private List<Doctor> doctorList;

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.nameTextView.setText(doctor.getUsername());
        holder.specializationTextView.setText(doctor.getSpecialization());  // Changed from "specialty" to "specialization"
        holder.addressTextView.setText(doctor.getAddress());
        holder.phoneTextView.setText(doctor.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView specializationTextView;
        TextView addressTextView;
        TextView phoneTextView;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name); // Check this ID
            specializationTextView = itemView.findViewById(R.id.tv_specialization);
            addressTextView = itemView.findViewById(R.id.tv_address);
            phoneTextView = itemView.findViewById(R.id.tv_phone);
        }
    }
}
