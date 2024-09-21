package com.example.klinik_android; // Replace with your actual package name

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Find buttons by their ID
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnSignUp = findViewById(R.id.btn_sign_up);
        Button btnLogin = findViewById(R.id.btn_login);

        // Navigate to RegisterActivity when Sign Up button is clicked
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, RegisterActivity.class);
                startActivity(intent); // Start the registration activity
            }
        });

        // Navigate to LoginActivity when Login button is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, LoginActivity.class);
                startActivity(intent); // Start the login activity
            }
        });
    }
}
