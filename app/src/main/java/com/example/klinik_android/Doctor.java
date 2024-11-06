package com.example.klinik_android;

public class Doctor {
    private String physID;
    private String username;
    private String specialization;
    private String address;
    private String phoneNo;
    private String description;
    private String photoUrl; // Add this field

    // Constructor, getters, and setters

    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Doctor(String physID, String username, String specialization, String address, String phoneNo, String description, String photoUrl) {
        this.physID = physID;
        this.username = username;
        this.specialization = specialization;
        this.address = address;
        this.phoneNo = phoneNo;
        this.description = description;
        this.photoUrl = photoUrl;
    }

    public String getPhysID() { return physID; }
    public String getUsername() { return username; }
    public String getSpecialization() { return specialization; }
    public String getAddress() { return address; }
    public String getPhoneNo() { return phoneNo; }
    public String getDescription() { return description; }
    public String getPhotoUrl() { return photoUrl; } // Add getter for photoUrl

    // Add setter if needed
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
