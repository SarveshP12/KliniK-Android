package com.example.klinik_android;

public class Appointment {
    private String appointmentDate;
    private String appointmentTime;
    private String doctorName;
    private String patientName;

    // Default constructor required for calls to DataSnapshot.getValue(Appointment.class)
    public Appointment() {}

    public Appointment(String appointmentDate, String appointmentTime, String doctorName, String patientName) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.doctorName = doctorName;
        this.patientName = patientName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getPatientName() {
        return patientName;
    }
}
