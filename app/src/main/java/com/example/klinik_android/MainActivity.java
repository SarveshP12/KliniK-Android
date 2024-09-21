package com.example.klinik_android; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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
                // Create an Intent to start NewActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent); // Start the new activity
            }
        });
    }
}
