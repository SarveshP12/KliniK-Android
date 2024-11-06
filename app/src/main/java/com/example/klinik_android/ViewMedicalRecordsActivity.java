package com.example.klinik_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ViewMedicalRecordsActivity extends AppCompatActivity {

    private static final String TAG = "ViewMedicalRecordsActivity";
    private static final int PICK_PDF_REQUEST = 1;

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private LinearLayout recordsLayout;
    private ProgressBar loadingSpinner;
    private TextView noRecordsText;
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_medical_records);

        recordsLayout = findViewById(R.id.records_layout);
        loadingSpinner = findViewById(R.id.loading_spinner);
        noRecordsText = findViewById(R.id.no_records_text);
        Button addRecordButton = findViewById(R.id.add_record_button);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get patientId from intent
        Intent intent = getIntent();
        patientId = intent.getStringExtra("patientId");

        if (patientId != null) {
            loadMedicalRecords(patientId);
        } else {
            Toast.makeText(this, "Patient ID is missing.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up Add New Record button
        addRecordButton.setOnClickListener(v -> openFilePicker());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });
    }

    private void loadMedicalRecords(String patientId) {
        loadingSpinner.setVisibility(View.VISIBLE);

        CollectionReference recordsRef = firestore.collection("patients")
                .document(patientId)
                .collection("medicalrecords");

        recordsRef.get().addOnCompleteListener(task -> {
            loadingSpinner.setVisibility(View.GONE);
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("title");
                    String pdfUrl = document.getString("pdfUrl");

                    // Display each record as a clickable item
                    TextView recordView = new TextView(this);
                    recordView.setText(title);
                    recordView.setTextSize(18);
                    recordView.setPadding(16, 16, 16, 16);
                    recordView.setOnClickListener(v -> openPdf(pdfUrl));

                    recordsLayout.addView(recordView);
                }
            } else {
                noRecordsText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri pdfUri = data.getData();
            if (pdfUri != null) {
                uploadPdf(pdfUri);
            }
        }
    }

    private void uploadPdf(Uri pdfUri) {
        String fileName = getFileName(pdfUri);
        StorageReference storageRef = storage.getReference().child("medical_records/" + patientId + "/" + fileName);

        loadingSpinner.setVisibility(View.VISIBLE);
        noRecordsText.setVisibility(View.GONE);

        storageRef.putFile(pdfUri).addOnSuccessListener(taskSnapshot ->
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String pdfUrl = uri.toString();
                    saveRecordToFirestore(fileName, pdfUrl);
                })).addOnFailureListener(e -> {
            loadingSpinner.setVisibility(View.GONE);
            Toast.makeText(this, "Failed to upload PDF", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error uploading PDF", e);
        });
    }

    private void saveRecordToFirestore(String title, String pdfUrl) {
        Map<String, Object> record = new HashMap<>();
        record.put("title", title);
        record.put("pdfUrl", pdfUrl);
        record.put("date", System.currentTimeMillis());

        firestore.collection("patients").document(patientId)
                .collection("medicalrecords").add(record)
                .addOnSuccessListener(documentReference -> {
                    loadingSpinner.setVisibility(View.GONE);
                    loadMedicalRecords(patientId); // Reload records
                    Toast.makeText(this, "Record added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    loadingSpinner.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to add record", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error adding record", e);
                });
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        return result != null ? result : "document.pdf";
    }

    private void openPdf(String pdfUrl) {
        Intent intent = new Intent(this, PdfViewerActivity.class);
        intent.putExtra("pdfUrl", pdfUrl);
        startActivity(intent);
    }
}
