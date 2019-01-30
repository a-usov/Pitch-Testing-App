package com.example.conal.soundrecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Trials extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE = 1000;
    //Declare variables
    //private Button createPDF, createCSV;
    private Button createCSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trials);

        createCSV = this.<Button>findViewById(R.id.btnCSV);

        createCSV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    generateCSV();
                }
                catch(IOException e) {

                }
            }
        });


    }
       /*
        createPDF = (Button) findViewById(R.id.btnPDF);

        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateReportPDF();
            }
        });



    public Document generateReportPDF(){
        //create document
        Document report = new Document();
        String dest = "";

        //location to save
        PdfWriter.getInstance(report, new FileOutputStream("C:\Users\Olga\Documents.test_pdf.pdf"));

        //open to write
        report.open();
    }*/

    public void generateCSV() throws IOException {
        if (checkPermissionFromDevice()) {
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/CSV Files");

            boolean var = false;
            if (!folder.exists()) var = folder.mkdir();

            System.out.println("" + var);

            final String filename = folder.toString() + "/" + "Test.csv";

            try {
                FileWriter fw = new FileWriter(filename);

                fw.append("data");
                fw.append(',');

                fw.append("goes");
                fw.append(',');

                fw.append("here:");
                fw.append(',');

                fw.append("time");
                fw.append(',');

                fw.append("0.6");
                fw.append(',');

                fw.append("height");
                fw.append(',');

                fw.append("1.4");
                fw.append(',');

                fw.append('\n');

                fw.close();

                Toast.makeText(this, "Written to file \"" + folder.toString() + filename + "/\"", Toast.LENGTH_SHORT).show();
            } catch (Exception e) { }
        }

        else {
            requestPermission();
            this.finish();
            Toast.makeText(this, "Cannot create CSV without permission.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openHomePage(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void openMyTestsPage(){
        Intent intent = new Intent(this, MyTests.class);
        startActivity(intent);
    }

    //dropdown menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home){
            openHomePage();
        }else if(item.getItemId() == R.id.my_tests){
            openMyTestsPage();
        }else{
            Toast.makeText(this, "This will be My Account page", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //
    // Next 3 methods copied from Main Activity. Need to check permissions before creating CSV/PDF.
    //

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }

    public boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}
