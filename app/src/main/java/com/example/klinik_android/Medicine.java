package com.example.klinik_android;

public class Medicine {
    private String medicine_name;
    private String expiry_date;
    private double price;
    private int quantity;

    public Medicine() {
        // Required empty constructor for Firebase
    }

    public Medicine(String medicine_name, String expiry_date, double price, int quantity) {
        this.medicine_name = medicine_name;
        this.expiry_date = expiry_date;
        this.price = price;
        this.quantity = quantity;
    }

    public String getMedicineName() {
        return medicine_name;
    }

    public void setMedicineName(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getExpiryDate() {
        return expiry_date;
    }

    public void setExpiryDate(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
