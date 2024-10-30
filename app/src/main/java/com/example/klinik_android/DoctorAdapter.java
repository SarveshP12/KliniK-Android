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
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        // Bind each doctor data to the respective view holder
        Doctor doctor = doctorList.get(position);
        holder.doctorName.setText(doctor.getName());
        holder.doctorSpecialty.setText(doctor.getSpecialty());
        holder.doctorAddress.setText(doctor.getAddress());
        holder.doctorPhone.setText(doctor.getPhone());
        holder.doctorPhysId.setText(String.valueOf(doctor.getPhysID()));
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    // ViewHolder class to hold reference to each item in the RecyclerView
    public static class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName;
        TextView doctorSpecialty;
        TextView doctorAddress;
        TextView doctorPhone;
        TextView doctorPhysId;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorNameTextView);
            doctorSpecialty = itemView.findViewById(R.id.doctorSpecialtyTextView);
            doctorAddress = itemView.findViewById(R.id.doctorAddressTextView);
            doctorPhone = itemView.findViewById(R.id.doctorPhoneTextView);
            doctorPhysId = itemView.findViewById(R.id.doctorPhysIdTextView);
        }
    }
}
