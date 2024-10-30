package com.example.klinik_android; // Replace with your actual package name

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;  // Request code for permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button in the activity_main.xml layout file
        Button btnOpenNewActivity = findViewById(R.id.btn_open_new_activity);

        // Set an onClickListener to handle the button click
        btnOpenNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start SecondActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent); // Start the new activity
            }
        });

        // SOS Button functionality (LinearLayout instead of Button)
//        LinearLayout btnSos = findViewById(R.id.btn_medical_records);
//        btnSos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Check if permission is granted
//                if (ContextCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//
//                    // Request permission
//                    ActivityCompat.requestPermissions(MainActivity.this,
//                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
//                } else {
//                    // Permission granted, make the call
//                    makeSOSCall();
//                }
//            }
//        });
//    }
//
//    // Method to make the SOS call
//    private void makeSOSCall() {
//        // Replace this with the actual healthcare number
//        String phoneNumber = "tel:9833883314";
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse(phoneNumber));
//
//        try {
//            startActivity(callIntent);
//        } catch (SecurityException e) {
//            Toast.makeText(this, "Permission to make calls not granted", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Handle permission result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CALL_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//                makeSOSCall();
//            } else {
//                // Permission denied
//                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
