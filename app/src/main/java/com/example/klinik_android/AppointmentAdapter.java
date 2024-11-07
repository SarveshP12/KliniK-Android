package com.example.klinik_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<Appointment> appointmentList;

    public AppointmentAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.appointmentDateTextView.setText("Date: " + appointment.getAppointmentDate());
        holder.appointmentTimeTextView.setText("Time: " + appointment.getAppointmentTime());
        holder.doctorNameTextView.setText("Doctor: " + appointment.getDoctorName());
        holder.patientNameTextView.setText("Patient: " + appointment.getPatientName());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentDateTextView, appointmentTimeTextView, doctorNameTextView, patientNameTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
            appointmentTimeTextView = itemView.findViewById(R.id.appointmentTimeTextView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
        }
    }
}
