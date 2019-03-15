package com.example.conal.soundrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;
import static com.example.conal.soundrecord.MapsActivity.TEST;

public class FinalActivity extends AppCompatActivity {

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
    private String fifaProString;
    private String uk1String = "";
    private String uk2String = "";
    private String flight3String = "";
    private String flight4String = "";
    private String flight5tring = "";
    private String otherEquip;


    private final List<TextView> tableDrops = new ArrayList<>();
    private List<Double> avgHeightsList = new ArrayList<>();
    private final DecimalFormat df = new DecimalFormat("0.00");
    private PitchTest finalTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defaultValue = "defaultValueIfNothingIsFound";

        // SETUP ALL DATA FOR TABLE/CSV/PDF
        currentDate = sharedPref.getString("currentDate", defaultValue);
        currentTime = sharedPref.getString("currentTime", defaultValue);
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
        otherEquip = sharedPref.getString("other", defaultValue);

        boolean uk1 = sharedPref.getBoolean("uk1", false);
        boolean uk2 = sharedPref.getBoolean("uk2", false);
        boolean flight3 = sharedPref.getBoolean("flight3", false);
        boolean flight4 = sharedPref.getBoolean("flight4", false);
        boolean flight5 = sharedPref.getBoolean("flight5", false);
        boolean fifaPro = sharedPref.getBoolean("fifaPro", false);

        // Equipment radio buttons
        String x = "X";
        if (uk1) uk1String = x;
        if (uk2) uk2String = x;
        if (flight3) flight3String = x;
        if (flight4) flight4String = x;
        if (flight5) flight5tring = x;

        if (fifaPro)
            fifaProString = "FIFA Quality Pro: 0.6 - 0.85m";
        else
            fifaProString = "FIFA Quality: 0.6 - 1.0m";



        //Get the pitch tests
        intent = getIntent();
        finalTest = intent.getParcelableExtra(MapsActivity.TEST); // Get the final complete pitch test

        TextView txtViewTotalAvg = this.findViewById(R.id.textView56);
        System.out.println(finalTest.getTotalAvg());
        txtViewTotalAvg.setText(df.format(finalTest.getTotalAvg()));

        fillTable(finalTest);
        fillAverages(finalTest);

        try {
            generateCSV(finalTest);
        } //Change this to generateCSV(pitchTest)
        catch (IOException e) {
            Toast.makeText(FinalActivity.this, "Couldn't create CSV", Toast.LENGTH_SHORT).show();
        }

        Button createPDF = this.findViewById(R.id.btnPDF);
        Button newTest = this.findViewById(R.id.new_test_btn);
        Button discard = this.findViewById(R.id.discard_test_btn);

        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    generateReportPDF(finalTest);
                } catch (IOException e) {
                    Toast.makeText(FinalActivity.this, "Couldn't create PDF", Toast.LENGTH_SHORT).show();
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


    private void fillTable(PitchTest pitchTest) { // Filling table for all drops bar the averages
        List<TextView> tableCells = initializeTable();
        List<Double> heights = pitchTest.organiseHeights();

        for (int i = 0; i < heights.size(); i++) {
            tableCells.get(i).setText(df.format(heights.get(i)));
        }
    }

    private void fillAverages(PitchTest pitchTest) {
        List<Double> avgHeights = pitchTest.getRunningAverages();

        TextView txtViewAvg1 = this.findViewById(R.id.textView50);
        TextView txtViewAvg2 = this.findViewById(R.id.textView51);
        TextView txtViewAvg3 = this.findViewById(R.id.textView52);
        TextView txtViewAvg4 = this.findViewById(R.id.textView53);
        TextView txtViewAvg5 = this.findViewById(R.id.textView54);
        TextView txtViewAvg6 = this.findViewById(R.id.textView55);

        for (int i = 1; i < 6; i++) {
            try {
                avgHeights.get(i);
            } catch (IndexOutOfBoundsException e) {
                avgHeights.add(0.0);
            }
        }

        txtViewAvg6.setText(df.format(avgHeights.get(5)));
        txtViewAvg5.setText(df.format(avgHeights.get(4)));
        txtViewAvg4.setText(df.format(avgHeights.get(3)));
        txtViewAvg3.setText(df.format(avgHeights.get(2)));
        txtViewAvg2.setText(df.format(avgHeights.get(1)));
        txtViewAvg1.setText(df.format(avgHeights.get(0)));

        avgHeightsList = avgHeights;
    }

    private List<TextView> initializeTable() {
        // Location 1
        tableDrops.add((TextView) this.findViewById(R.id.textView10));
        tableDrops.add((TextView) this.findViewById(R.id.textView18));
        tableDrops.add((TextView) this.findViewById(R.id.textView26));
        tableDrops.add((TextView) this.findViewById(R.id.textView34));
        tableDrops.add((TextView) this.findViewById(R.id.textView42));

        // Location 2 drops or column 2
        tableDrops.add((TextView) this.findViewById(R.id.textView11));
        tableDrops.add((TextView) this.findViewById(R.id.textView19));
        tableDrops.add((TextView) this.findViewById(R.id.textView27));
        tableDrops.add((TextView) this.findViewById(R.id.textView35));
        tableDrops.add((TextView) this.findViewById(R.id.textView43));

        // Location 3 drops or column 3
        tableDrops.add((TextView) this.findViewById(R.id.textView12));
        tableDrops.add((TextView) this.findViewById(R.id.textView20));
        tableDrops.add((TextView) this.findViewById(R.id.textView28));
        tableDrops.add((TextView) this.findViewById(R.id.textView36));
        tableDrops.add((TextView) this.findViewById(R.id.textView44));

        // Location 4 drops or column 4
        tableDrops.add((TextView) this.findViewById(R.id.textView13));
        tableDrops.add((TextView) this.findViewById(R.id.textView21));
        tableDrops.add((TextView) this.findViewById(R.id.textView29));
        tableDrops.add((TextView) this.findViewById(R.id.textView37));
        tableDrops.add((TextView) this.findViewById(R.id.textView45));

        // Location 5 drops or column 5
        tableDrops.add((TextView) this.findViewById(R.id.textView14));
        tableDrops.add((TextView) this.findViewById(R.id.textView22));
        tableDrops.add((TextView) this.findViewById(R.id.textView30));
        tableDrops.add((TextView) this.findViewById(R.id.textView38));
        tableDrops.add((TextView) this.findViewById(R.id.textView46));

        // Location 6 drops or column 6
        tableDrops.add((TextView) this.findViewById(R.id.textView15));
        tableDrops.add((TextView) this.findViewById(R.id.textView23));
        tableDrops.add((TextView) this.findViewById(R.id.textView31));
        tableDrops.add((TextView) this.findViewById(R.id.textView39));
        tableDrops.add((TextView) this.findViewById(R.id.textView47));

        return tableDrops;
    }

    private void generateCSV(PitchTest pitchTest) throws IOException { // Change this method to take PitchTest
        File folder = new File(Environment.getExternalStorageDirectory() + "/CSV Files");

        if (!folder.exists())
            if (!folder.mkdir())
                throw new IOException();

        final String filename = folder.toString() + "/" + currentDate + "_" + currentTime
                + ":" + Calendar.getInstance().get(Calendar.SECOND) + ".csv";


        FileWriter fw = new FileWriter(filename);
        fw.append(" ");
        fw.append(",");
        fw.append("Drop 1");
        fw.append(",");
        fw.append("Drop 2");
        fw.append(",");
        fw.append("Drop 3");
        fw.append(",");
        fw.append("Drop 4 ");
        fw.append(",");
        fw.append("Drop 5");
        fw.append(",");
        fw.append("Average");
        fw.append("\n");


        for (Location loc : pitchTest.getLocations()) {
            int locationIndex = pitchTest.getLocations().indexOf(loc) + 1;
            fw.append("Location").append(String.valueOf(locationIndex));
            fw.append(",");

            for(int i = 0; i < 5; i++){
                try {
                    fw.append(df.format(loc.getResults().get(i).getBounceHeight()));
                } catch (Exception e){
                } finally {
                    fw.append(",");
                }
            }

            Double avgBounceHeight = loc.getRunningAvg();
            fw.append(df.format(avgBounceHeight));
            fw.append("\n");
        }

        fw.append("\n");
        fw.append("Overall average");
        fw.append(",");
        fw.append(df.format(pitchTest.getTotalAvg()));
        fw.append("\n");

        fw.append("Test Date");
        fw.append(",");
        fw.append(currentDate);
        fw.append("\n");

        fw.append("Job number");
        fw.append(",");
        fw.append(jobNo);
        fw.append("\n");

        fw.append("Client");
        fw.append(",");
        fw.append(client);
        fw.append("\n");

        fw.append("Lead technician");
        fw.append(",");
        fw.append(leadTechnician);


        fw.close();

        Toast.makeText(this, "Created CSV file", Toast.LENGTH_SHORT).show();
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

    private void generateReportPDF(PitchTest pitchTest) throws IOException {
        //create folder
        File folderPDF = new File(Environment.getExternalStorageDirectory() + "/PDF reports");

        List<Double> allHeights = pitchTest.organiseHeights();

        if (!folderPDF.exists())
            if (!folderPDF.mkdir())
                throw new IOException();

        String filePDF = folderPDF.toString() + "/" + currentDate + "_" + currentTime
                + ":" + Calendar.getInstance().get(Calendar.SECOND) + ".pdf";

        //create fonts
        Font title = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
        Font labelBlack = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
        Font labelRed = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.RED);
        Font other = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
        LatLng coord = pitchTest.getLocation(0).getLocation();

        if (allHeights.size() < 30) {
            for (int i = allHeights.size(); i < 30; i++) {
                allHeights.add(0.0); // fill up the heights if nothing is there
            }
        }

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

            insertCell(table, "Test Results (m)", Element.ALIGN_MIDDLE, 1, 6, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 1", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 2", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 3", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 4", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 5", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Test Location 6", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    insertCell(table, df.format(allHeights.get(i + (5 * j))), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
                }
            }

            double avgHeight = 0;

            insertCell(table, "Mean Result", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);

            for (int i = 0; i < 6; i++){
                insertCell(table, df.format(avgHeightsList.get(i)), Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
                avgHeight += avgHeightsList.get(i);
            }

            insertCell(table, "Consistency ±10%", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);

            avgHeight = avgHeight/6;
            Log.i("consistency", "avgHeight " + avgHeight);
            double avgHeightPercent = avgHeight / 100;
            Log.i("consistency", "avgHeightPercent " + avgHeightPercent);

            for (Double height : avgHeightsList) {
                double consistency = (height - avgHeight) / avgHeightPercent;
                Log.i("consistency", "height " + height);
                Log.i("consistency", "consistency " + consistency);
                insertCell(table, df.format(consistency), Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            }



            /*insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, "###", Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);*/

            //picture cell
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

            Toast.makeText(FinalActivity.this, "PDF created.", Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            throw new IOException();
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void openPDFActivity() {
        intent = getIntent();
        intent.removeExtra(TEST);
        intent.setClass(this, FormPDFActivity.class);
        startActivity(intent);

    }

    private void openHomePage() {
        intent = getIntent();
        intent.removeExtra(TEST);
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
    }
}