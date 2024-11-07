package com.example.klinik_android;

public class Medicine {
    private int medicineID;
    private String medicine_name;
    private int quantity;
    private double price;
    private String expiry_date;

    // No-argument constructor required for Firebase
    public Medicine() {}

    public Medicine(int medicineID, String medicine_name, int quantity, double price, String expiry_date) {
        this.medicineID = medicineID;
        this.medicine_name = medicine_name;
        this.quantity = quantity;
        this.price = price;
        this.expiry_date = expiry_date;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getExpiry_date() {
        return expiry_date;
    }
}
