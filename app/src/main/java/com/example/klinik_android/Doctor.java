package com.example.klinik_android;

public class Doctor {
    private int physID;
    private String name;
    private String address;
    private String phone;
    private String specialty;

    // Default constructor required for calls to DataSnapshot.getValue(Doctor.class)
    public Doctor() {
    }

    // Parameterized constructor for easier initialization if needed
    public Doctor(int physID, String name, String address, String phone, String specialty) {
        this.physID = physID;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.specialty = specialty;
    }

    // Getters and setters for each field
    public int getPhysID() {
        return physID;
    }

    public void setPhysID(int physID) {
        this.physID = physID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
