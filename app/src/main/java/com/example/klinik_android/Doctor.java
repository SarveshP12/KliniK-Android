package com.example.klinik_android;

public class Doctor {
    private String username;
    private String specialization;
    private String address;
    private String phoneNo;
    private String physID;
    private String description;

    // Required empty constructor for Firestore
    public Doctor() {}

    public Doctor(String username, String specialization, String address, String phoneNo, String physID, String description) {
        this.username = username;
        this.specialization = specialization;
        this.address = address;
        this.phoneNo = phoneNo;
        this.physID = physID;
        this.description = description;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPhysID() {
        return physID;
    }

    public String getDescription() {
        return description;
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

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setPhysID(String physID) {
        this.physID = physID;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
