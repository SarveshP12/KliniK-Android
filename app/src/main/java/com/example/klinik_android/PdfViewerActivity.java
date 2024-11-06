package com.example.klinik_android;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class PdfViewerActivity extends AppCompatActivity {

    private static final String TAG = "PdfViewerActivity";
    private ImageView pdfPageImageView;
    private Button nextPageButton, prevPageButton;
    private ProgressDialog progressDialog;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor fileDescriptor;
    private int currentPageIndex = 0;
    private String pdfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfPageImageView = findViewById(R.id.pdfPageImageView);
        nextPageButton = findViewById(R.id.nextPageButton);
        prevPageButton = findViewById(R.id.prevPageButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading PDF...");
        progressDialog.setCancelable(false);

        pdfUrl = getIntent().getStringExtra("pdfUrl");

        if (pdfUrl != null) {
            downloadPdfFile(pdfUrl);
        } else {
            Toast.makeText(this, "PDF URL not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        nextPageButton.setOnClickListener(v -> showPage(currentPageIndex + 1));
        prevPageButton.setOnClickListener(v -> showPage(currentPageIndex - 1));
    }

    private void downloadPdfFile(String pdfUrl) {
        progressDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(pdfUrl);

        // Define the local file path
        File localFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "temp.pdf");

        storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            openPdfRenderer(localFile);
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(PdfViewerActivity.this, "Failed to load PDF", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error downloading PDF", e);
        });
    }

    private void openPdfRenderer(File file) {
        try {
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fileDescriptor);
            showPage(0);
        } catch (IOException e) {
            Toast.makeText(this, "Failed to open PDF", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error opening PDF renderer", e);
        }
    }

    private void showPage(int index) {
        if (pdfRenderer == null || index < 0 || index >= pdfRenderer.getPageCount()) return;

        if (currentPage != null) {
            currentPage.close();
        }

        currentPage = pdfRenderer.openPage(index);
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        pdfPageImageView.setImageBitmap(bitmap);

        currentPageIndex = index;

        // Update button visibility based on the current page index
        prevPageButton.setVisibility(currentPageIndex == 0 ? View.INVISIBLE : View.VISIBLE);
        nextPageButton.setVisibility(currentPageIndex == pdfRenderer.getPageCount() - 1 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        try {
            if (currentPage != null) {
                currentPage.close();
            }
            if (pdfRenderer != null) {
                pdfRenderer.close();
            }
            if (fileDescriptor != null) {
                fileDescriptor.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing PDF renderer", e);
        }
        super.onDestroy();
    }
}
