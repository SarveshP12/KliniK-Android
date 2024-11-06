package com.example.klinik_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PatientDashboard extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_PERMISSION = 100;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageView profileImageView;
    private TextView usernameTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Initialize Firebase Auth, Firestore, and Storage
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Find views
        profileImageView = findViewById(R.id.profile_image);
        usernameTextView = findViewById(R.id.profile_name);

        // Fetch and display the username from Firestore
        fetchAndDisplayUsername();

        // Set up click listener for profile image to pick an image from gallery
        profileImageView.setOnClickListener(v -> checkAndRequestPermissions());

        // Set up button clicks for different actions
        setupButtons();
    }

    // Method to fetch the username from Firestore and display it in the TextView
    private void fetchAndDisplayUsername() {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("patients").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the username
                    String username = document.getString("username");
                    usernameTextView.setText("Welcome, " + (username != null ? username : "Patient"));

                    // Get the profile image URL from Firestore and set it in the ImageView
                    String profileImageUrl = document.getString("profileImageUrl");
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        // Load the image using Glide or Picasso
                        Glide.with(this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.circle_background)  // Placeholder image
                                .into(profileImageView);
                    }
                } else {
                    Toast.makeText(PatientDashboard.this, "User data not found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PatientDashboard.this, "Failed to fetch user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set up click listeners for the buttons
    private void setupButtons() {
        findViewById(R.id.btn_book_appointment).setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboard.this, FindDoctor.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_order_medicine).setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboard.this, OrderMedicine.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_medical_records).setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboard.this, MedicalRecord.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_telemedicine).setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboard.this, TelemedicineActivity.class);
            startActivity(intent);
        });
    }

    // Method to check and request permissions if needed
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    // Method to pick an image from the gallery
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    // Handle the result from the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
            uploadImageToFirebase(selectedImageUri);
        }
    }

    // Method to upload image to Firebase Storage and save the URL in Firestore
    private void uploadImageToFirebase(Uri imageUri) {
        String userId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = storage.getReference().child("profile_images/" + userId + ".jpg");

        UploadTask uploadTask = storageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();
            db.collection("patients").document(userId).update("profileImageUrl", downloadUrl)
                    .addOnSuccessListener(aVoid -> Toast.makeText(PatientDashboard.this, "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(PatientDashboard.this, "Failed to save image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        })).addOnFailureListener(e -> Toast.makeText(PatientDashboard.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
