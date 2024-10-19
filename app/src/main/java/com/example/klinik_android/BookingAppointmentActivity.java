package com.example.klinik_android;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class BookingAppointmentActivity extends AppCompatActivity {

    private EditText patientNameInput;
    private TextView dateDisplay, timeDisplay;
    private Spinner doctorSpinner;
    private Button selectDateButton, selectTimeButton, submitAppointmentButton;

    private Calendar calendar;
    private String selectedDoctor;
    private ArrayList<String> doctorList = new ArrayList<>();
    private ArrayAdapter<String> doctorAdapter;

    private DatabaseReference doctorDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);

        // Initialize the views
        patientNameInput = findViewById(R.id.patient_name_input);
        dateDisplay = findViewById(R.id.date_display);
        timeDisplay = findViewById(R.id.time_display);
        selectDateButton = findViewById(R.id.select_date_button);
        selectTimeButton = findViewById(R.id.select_time_button);
        doctorSpinner = findViewById(R.id.doctor_spinner);
        submitAppointmentButton = findViewById(R.id.submit_appointment_button);

        calendar = Calendar.getInstance();

        // Initialize Firebase Database reference
        doctorDatabaseRef = FirebaseDatabase.getInstance().getReference("doctors");

        // Initialize Doctor Spinner Adapter
        doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorList);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(doctorAdapter);

        // Fetch doctors from Firebase
        fetchDoctorsFromFirebase();

        // Handle doctor selection
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedDoctor = doctorList.get(position);  // Save the selected doctor
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where no doctor is selected
            }
        });

        // Date selection using DatePickerDialog
        selectDateButton.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingAppointmentActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        dateDisplay.setText(selectedDate);  // Update the date display
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Time selection using TimePickerDialog
        selectTimeButton.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(BookingAppointmentActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        String selectedTime = selectedHour + ":" + String.format("%02d", selectedMinute);
                        timeDisplay.setText(selectedTime);  // Update the time display
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        // Handle submit button click
        submitAppointmentButton.setOnClickListener(v -> {
            String patientName = patientNameInput.getText().toString();
            String appointmentDate = dateDisplay.getText().toString();
            String appointmentTime = timeDisplay.getText().toString();

            if (patientName.isEmpty() || appointmentDate.equals("Select Date") || appointmentTime.equals("Select Time")) {
                Toast.makeText(BookingAppointmentActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Process the appointment booking (you could save it to Firebase here)
                String confirmationMessage = "Appointment booked for " + patientName +
                        " with " + selectedDoctor + " on " + appointmentDate + " at " + appointmentTime;
                Toast.makeText(BookingAppointmentActivity.this, confirmationMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Fetch doctors from Firebase and populate the Spinner
    private void fetchDoctorsFromFirebase() {
        doctorDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();  // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String doctorName = snapshot.child("name").getValue(String.class);
                    if (doctorName != null) {
                        doctorList.add(doctorName);  // Add doctor name to the list
                    }
                }
                doctorAdapter.notifyDataSetChanged();  // Notify the adapter to update the Spinner
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookingAppointmentActivity.this, "Failed to load doctors", Toast.LENGTH_SHORT).show();
            }
        });
    }
}