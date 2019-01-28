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
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Trials extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE = 1000;
    //Declare variables
    //private Button createPDF, createCSV;
    private Button createCSV;
    private Button createPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trials);

        createCSV = this.<Button>findViewById(R.id.btnCSV);
        createPDF = (Button) findViewById(R.id.btnPDF);

        createCSV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    generateCSV();
                }
                catch(IOException e) {

                }
            }
        });

        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    generateReportPDF();

                } catch (IOException e){}
            }
        });
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim()));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
    }

    public void generateReportPDF() throws IOException {

        //create folder
        File folderPDF = new File(Environment.getExternalStorageDirectory() + "/PDF reports");

        if(!folderPDF.exists()) folderPDF.mkdir();

        String filePDF = folderPDF.toString() + "/" + "Report.pdf";

        try{
            Document report = new Document();
            report.setPageSize(PageSize.A4);
            PdfWriter.getInstance(report, new FileOutputStream(filePDF));

            report.open();

            float [] pointColumnWidths = {150F, 150F, 150F, 150F};
            PdfPTable table = new PdfPTable(pointColumnWidths);

            // Adding cells to the table
            insertCell(table, "Determination of Ball Rebound (FIELD)\n" +
                    "FIFA Quality concept October 2015\n", Element.ALIGN_CENTER, 4);
            insertCell(table, "Job No", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Contract", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Test Date", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Test Condition", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Substrate Type", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Surface Name", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Time of day", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Air temp: (ºC)", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Surface temp: (ºC)", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Humidity: (%)", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Wind Speed: (m/s)", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Substrate Type", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Day Book", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Client", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Date of Construction", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Carpet Type", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Infill Type", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Shockpad", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Weather Conditions", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Lead Technician", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Additional Technician", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);
            insertCell(table, "Uncertainty Measurement:", Element.ALIGN_LEFT, 1);
            insertCell(table, "", Element.ALIGN_LEFT, 1);


            /*
            table.addCell(new PdfPCell(new Phrase("Job No")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Contract")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Test Date")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Test Condition")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Substrate Type")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Surface Name")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Time of day")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Air temp: (ºC)")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Surface temp: (ºC)")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Humidity: (%)")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Wind Speed: (m/s)")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Substrate Type")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Day Book")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Client")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Date of Construction")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Carpet Type")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Infill Type")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Shockpad")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Weather Conditions")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Lead Technician")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Additional Technician")));
            table.addCell(new PdfPCell());
            table.addCell(new PdfPCell(new Phrase("Uncertainty Measurement:")));
            table.addCell(new PdfPCell());*/


            report.add(table);
            report.close();

        }catch(Exception e){}

    }

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
