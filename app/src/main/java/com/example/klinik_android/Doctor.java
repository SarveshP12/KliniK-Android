package com.example.klinik_android;

public class Doctor {
    private String username;
    private String specialization;  // Changed from "specialty" to "specialization"
    private String address;
    private String phoneNumber;

    // Required empty constructor for Firestore
    public Doctor() {}

    public Doctor(String username, String specialization, String address, String phoneNumber) {
        this.username = username;
        this.specialization = specialization;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}