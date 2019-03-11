package com.example.conal.soundrecord;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;
import static com.example.conal.soundrecord.MapsActivity.TEST;
import static com.example.conal.soundrecord.RecordingActivity.DEVICE;

public class FinalActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_CODE = 1000;
    private Intent intent;

    private String currentDate;
    private String currentTime;
    private String jobNo;
    private String contract;
    private String surfaceName;
    private String airTemp;
    private String surfaceTemp;
    private String humidity;
    private String windSpeed;
    private String dayBook;
    private String client;
    private String dateOfConstruction;
    private String carpetType;
    private String infillType;
    private String shockpad;
    private String weatherConditions;
    private String leadTechnician;
    private String additionalTechnician;
    private String substrateType;
    private String testCondition;
    private String uncertainityMeasurement;
    private Boolean fifaPro;
    private String fifaProString;
    private Boolean uk1;
    private Boolean uk2;
    private Boolean flight3;
    private Boolean flight4;
    private Boolean flight5;
    private String X = "X";
    private String uk1String = "";
    private String uk2String = "";
    private String flight3String = "";
    private String flight4String = "";
    private String flight5tring = "";
    private String otherEquip;

    private String defaultValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        defaultValue = "defaultValueIfNothingIsFound";
        /** Get the shared preferences **/
        setContentView(R.layout.activity_final);
        SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        /** Get date and time taken from home **/
        String dateKey = "currentDate";
        String dateTime = "currentTime";
        currentDate = sharedPref.getString("currentDate", "defaultStringIfNothingFound");
        currentTime = sharedPref.getString("currentTime", "defaultStringIfNothingFound");
        contract = sharedPref.getString("contract", defaultValue);
        surfaceName = sharedPref.getString("surfaceName", defaultValue);
        humidity = sharedPref.getString("humidity", defaultValue);
        airTemp = sharedPref.getString("airTemp", defaultValue);
        surfaceTemp = sharedPref.getString("surfaceTemp", defaultValue);
        windSpeed = sharedPref.getString("windSpeed", defaultValue);
        dayBook = sharedPref.getString("dayBook", defaultValue);
        client = sharedPref.getString("client", defaultValue);
        dateOfConstruction = sharedPref.getString("dateOfConstruction", defaultValue);
        carpetType = sharedPref.getString("carpetTye", defaultValue);
        infillType = sharedPref.getString("infillType", defaultValue);
        shockpad = sharedPref.getString("shockpad", defaultValue);
        weatherConditions = sharedPref.getString("weatherConditions", defaultValue);
        leadTechnician = sharedPref.getString("leadTechnician", defaultValue);
        additionalTechnician = sharedPref.getString("additionalTechnician", defaultValue);
        jobNo = sharedPref.getString("jobNo", defaultValue);
        uncertainityMeasurement = sharedPref.getString("uncertaintyMeasurement", defaultValue);
        testCondition = sharedPref.getString("testConditions", defaultValue);
        substrateType = sharedPref.getString("substrateType", defaultValue);
        fifaPro = sharedPref.getBoolean("fifaPro", false);

        uk1 = sharedPref.getBoolean("uk1", false);
        uk2 = sharedPref.getBoolean("uk2", false);
        flight3 = sharedPref.getBoolean("flight3", false);
        flight4 = sharedPref.getBoolean("flight4", false);
        flight5 = sharedPref.getBoolean("flight5", false);

        otherEquip = sharedPref.getString("other", defaultValue);


        if (fifaPro) fifaProString = "FIFA Quality Pro: 0.6 - 0.85m";
        else fifaProString = "FIFA Quality: 0.6 - 1.0m";

        /** Equipment radio buttons **/
        if (uk1) uk1String = X;
        if (uk2) uk2String = X;
        if (flight3) flight3String = X;
        if (flight4) flight4String = X;
        if (flight5) flight5tring = X;


        Log.i("currentDate", "The current date is " + currentDate);
        Log.i("currentDate", "The time recorded is " + currentTime);

        intent = getIntent();

        PitchTest test = intent.getParcelableExtra(MapsActivity.TEST);

        try {
            generateCSV(test);
        } //Change this to generateCSV(pitchTest)
        catch (IOException e) {
        }

        Button createPDF = this.findViewById(R.id.btnPDF);
        Button newTest = this.findViewById(R.id.new_test_btn);
        Button discard = this.findViewById(R.id.discard_test_btn);

        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    generateReportPDF();

                } catch (IOException e) {
                }
            }
        });

        newTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPDFActivity();
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, int rowspan, Font font, BaseColor color) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setBackgroundColor(color);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(15f);
        }
        table.addCell(cell);
    }

    public void generateCSV(PitchTest test) throws IOException { // Change this method to take PitchTest
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

                fw.append(test.toString());
                fw.append(',');

                fw.append('\n');

                fw.close();

                Toast.makeText(this, "Written to file \"" + folder.toString() + filename + "/\"", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        } else {
            requestPermission();
            this.finish();
            Toast.makeText(this, "Cannot create CSV without permission.", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateReportPDF() throws IOException {

        Toast.makeText(FinalActivity.this, "PDF created.", Toast.LENGTH_SHORT).show();
        //create folder
        File folderPDF = new File(Environment.getExternalStorageDirectory() + "/PDF reports");

        if (!folderPDF.exists()) folderPDF.mkdir();

        String filePDF = folderPDF.toString() + "/" + "Report.pdf";

        //create fonts
        Font title = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
        //Font subtitle = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK));
        Font labelBlack = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
        Font labelRed = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.RED);
        Font other = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
        Intent intent = getIntent();
        PitchTest test = intent.getParcelableExtra(MapsActivity.TEST);
        LatLng coord = test.getLocation(0).getLocation();

        try {
            Document report = new Document();
            report.setPageSize(PageSize.A4);
            PdfWriter.getInstance(report, new FileOutputStream(filePDF));

            report.open();

            float[] pointColumnWidths = {600F, 600F, 600F, 600F, 600F, 600F, 600F};
            PdfPTable table = new PdfPTable(pointColumnWidths);

            // Adding cells to the table
            insertCell(table, "Determination of Ball Rebound (FIELD)\n" +
                    "FIFA Quality concept October 2015\n", Element.ALIGN_CENTER, 7, 1, title, BaseColor.WHITE);
            insertCell(table, "Job No", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, jobNo, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Contract", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, contract, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Test Date", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, currentDate, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Test Condition", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, testCondition, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Substrate Type", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, substrateType, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Surface Name", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, surfaceName, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Time of day", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, currentTime, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Air temp: (ºC)", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, airTemp, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Surface temp: (ºC)", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, surfaceTemp, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Humidity: (%)", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, humidity, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Wind Speed: (m/s)", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            // insertCell(table, "", Element.ALIGN_LEFT, 2);
            insertCell(table, windSpeed, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Day Book", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, dayBook, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Client", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, client, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Date of Construction", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, dateOfConstruction, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Carpet Type", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, carpetType, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Infill Type", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, infillType, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Shockpad", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, shockpad, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Weather Conditions", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, weatherConditions, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Lead Technician", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, leadTechnician, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Additional Technician", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, additionalTechnician, Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Uncertainty Measurement:", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, uncertainityMeasurement, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, coord.toString(), Element.ALIGN_CENTER, 4, 1, other, BaseColor.WHITE);

            insertCell(table, "", Element.ALIGN_LEFT, 7, 1, labelBlack, BaseColor.BLACK);
            insertCell(table, "Calibrate ball on concrete", Element.ALIGN_CENTER, 7, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Limit: 1.35m ± 0.02m", Element.ALIGN_CENTER, 7, 1, labelRed, BaseColor.WHITE);

            insertCell(table, "Drop", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "1", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "2", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "3", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "4", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "5", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "AVG", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);

            insertCell(table, "Height (m)", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "", Element.ALIGN_LEFT, 7, 1, other, BaseColor.BLACK);

            insertCell(table, "Test ResultsActivity (m)", Element.ALIGN_MIDDLE, 1, 6, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 1", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 2", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 3", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 4", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 5", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 6", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);

            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, "Mean Result", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);

            insertCell(table, "Consistency ±10%", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);

            //picture cell
            try {
                InputStream ims = getAssets().open("pitch.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scalePercent(30);
                image.setAlignment(Element.ALIGN_CENTER);
                PdfPCell imageCell = new PdfPCell(image);
                imageCell.setRowspan(4);
                imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                imageCell.setColspan(7);
                table.addCell(imageCell);
            } catch (IOException e) {
                e.printStackTrace();
            }


            insertCell(table, fifaProString, Element.ALIGN_CENTER, 7, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "UK 1", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.YELLOW);
            insertCell(table, uk1String, Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Flight 3", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.BLUE);
            insertCell(table, flight3String, Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Flight 4", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.RED);
            insertCell(table, flight4String, Element.ALIGN_CENTER, 2, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "UK 2", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.GREEN);
            insertCell(table, uk2String, Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Flight 5 (Norway)", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.CYAN);
            insertCell(table, flight5tring, Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Other please state -", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.MAGENTA);
            insertCell(table, otherEquip, Element.ALIGN_CENTER, 2, 1, labelBlack, BaseColor.WHITE);

            report.add(table);

            report.close();

        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);

    }

    public boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    private void openPDFActivity() {
        intent = getIntent();
        intent.removeExtra(TEST);
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(DEVICE);
        editor.apply();
        intent.setClass(this, FormPDFActivity.class);
        startActivity(intent);

    }

    private void openHomePage() {
        intent = getIntent();
        intent.removeExtra(TEST);
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(DEVICE);
        editor.apply();
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
    }
}

