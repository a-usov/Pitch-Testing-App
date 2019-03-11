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
import android.widget.TextView;
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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;
import static com.example.conal.soundrecord.MapsActivity.TEST;

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


    private ArrayList<TextView> tableDrops = new ArrayList<>();


    private String defaultValue;
    private PitchTest finalTest;
    private ArrayList<Double> heightList = new ArrayList<Double>();
    private ArrayList<Double> avgHeightsList = new ArrayList<Double>();
    DecimalFormat df = new DecimalFormat("#.##");



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

        /** Get the pitch tests **/

        intent = getIntent();
        final PitchTest finalTest = intent.getParcelableExtra(MapsActivity.TEST); // Get the final complete pitch test


        final PitchTest testing = createTestPitchTest();

        ArrayList<Double> allHeights = fillTable(finalTest);
//        if(finalTest.getRunningAverages().size() == 5 ) {
//            fillAverages(finalTest);
//        }
//        ArrayList<Double> avgHeights = fillAverages(finalTest);
        TextView txtViewTotalAvg = this.findViewById(R.id.textView56);
        txtViewTotalAvg.setText(finalTest.getTotalAvg().toString());
//        fillTable(finalTest); // testing filling the table
        fillTable(finalTest);
        fillAverages(finalTest);






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



        try {
            generateCSV(finalTest);
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
                    generateReportPDF(finalTest);

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

    public PitchTest createTestPitchTest(){
        PitchTest pitchTest = new PitchTest();
        Location location1 = new Location(new LatLng(0,0));
        Location location2 = new Location(new LatLng(0,0));
        Location location3 = new Location(new LatLng(0,0));
        Location location4 = new Location(new LatLng(0,0));
        Location location5 = new Location(new LatLng(0,0));
        Location location6 = new Location(new LatLng(0,0));

        Result result1 = new Result(0,0,0);
        result1.setBounceHeight(1);

        Result result2 = new Result(0,0,0);
        result2.setBounceHeight(2);

        Result result3 = new Result(0,0,0);
        result3.setBounceHeight(new Double(3));

        Result result4 = new Result(0,0,0);
        result4.setBounceHeight(4);

        Result result5 = new Result(0,0,0);
        result5.setBounceHeight(5);

        location1.addResult(result1);
        location1.addResult(result2);
        location1.addResult(result3);
        location1.addResult(result4);
        location1.addResult(result5);

        Result result6 = new Result(0,0,0);
        result6.setBounceHeight(6);

        Result result7 = new Result(0,0,0);
        result7.setBounceHeight(7);

        Result result8 = new Result(0,0,0);
        result8.setBounceHeight(8);

        Result result9 = new Result(0,0,0);
        result9.setBounceHeight(9);

        Result result10 = new Result(0,0,0);
        result10.setBounceHeight(10);

        location2.addResult(result6);
        location2.addResult(result7);
        location2.addResult(result8);
        location2.addResult(result9);
        location2.addResult(result10);

        Result result11 = new Result(0,0,0);
        result11.setBounceHeight(11);

        Result result12 = new Result(0,0,0);
        result12.setBounceHeight(12);

        Result result13 = new Result(0,0,0);
        result13.setBounceHeight(13);

        Result result14 = new Result(0,0,0);
        result14.setBounceHeight(14);

        Result result15 = new Result(0,0,0);
        result15.setBounceHeight(15);

        location3.addResult(result11);
        location3.addResult(result12);
        location3.addResult(result13);
        location3.addResult(result14);
        location3.addResult(result15);

        Result result16 = new Result(0,0,0);
        result16.setBounceHeight(16);

        Result result17 = new Result(0,0,0);
        result17.setBounceHeight(17);

        Result result18 = new Result(0,0,0);
        result18.setBounceHeight(18);

        Result result19 = new Result(0,0,0);
        result19.setBounceHeight(19);

        Result result20 = new Result(0,0,0);
        result20.setBounceHeight(20);

        location4.addResult(result16);
        location4.addResult(result17);
        location4.addResult(result18);
        location4.addResult(result19);
        location4.addResult(result20);

        Result result21 = new Result(0,0,0);
        result21.setBounceHeight(21);

        Result result22 = new Result(0,0,0);
        result22.setBounceHeight(22);

        Result result23 = new Result(0,0,0);
        result23.setBounceHeight(23);

        Result result24 = new Result(0,0,0);
        result24.setBounceHeight(24);

        Result result25 = new Result(0,0,0);
        result25.setBounceHeight(25);

        location5.addResult(result21);
        location5.addResult(result22);
        location5.addResult(result23);
        location5.addResult(result24);
        location5.addResult(result25);

        Result result26 = new Result(0,0,0);
        result26.setBounceHeight(26);

        Result result27 = new Result(0,0,0);
        result27.setBounceHeight(27);

        Result result28 = new Result(0,0,0);
        result28.setBounceHeight(28);

        Result result29 = new Result(0,0,0);
        result29.setBounceHeight(29);

        Result result30 = new Result(0,0,0);
        result30.setBounceHeight(30);

        location6.addResult(result26);
        location6.addResult(result27);
        location6.addResult(result28);
        location6.addResult(result29);
        location6.addResult(result30);

        pitchTest.addLocation(location1);
        pitchTest.addLocation(location2);
        pitchTest.addLocation(location3);
        pitchTest.addLocation(location4);
        pitchTest.addLocation(location5);
        pitchTest.addLocation(location6);
        return pitchTest;
    }

    public void fillAverages(PitchTest pitchTest){
        ArrayList<Double> avgHeights = pitchTest.getRunningAverages();

        TextView txtViewAvg1 = this.findViewById(R.id.textView50);
        TextView txtViewAvg2 = this.findViewById(R.id.textView51);
        TextView txtViewAvg3 = this.findViewById(R.id.textView52);
        TextView txtViewAvg4 = this.findViewById(R.id.textView53);
        TextView txtViewAvg5 = this.findViewById(R.id.textView54);
        TextView txtViewAvg6 = this.findViewById(R.id.textView55);

        Log.i("pdf", "The number of average is " + avgHeights.size());
        if(avgHeights.size()== 6) {
            txtViewAvg6.setText(df.format(avgHeights.get(5)));
            txtViewAvg6.setText(df.format(avgHeights.get(4)));
            txtViewAvg6.setText(df.format(avgHeights.get(3)));
            txtViewAvg6.setText(df.format(avgHeights.get(2)));
            txtViewAvg6.setText(df.format(avgHeights.get(1)));
            txtViewAvg6.setText(df.format(avgHeights.get(0)));

        }else if(avgHeights.size()==5) {
            txtViewAvg6.setText(df.format(avgHeights.get(4)));
            txtViewAvg6.setText(df.format(avgHeights.get(3)));
            txtViewAvg6.setText(df.format(avgHeights.get(2)));
            txtViewAvg6.setText(df.format(avgHeights.get(1)));
            txtViewAvg6.setText(df.format(avgHeights.get(0)));
            avgHeights.add(5, 0.0);
        } else if(avgHeights.size()== 4) {
            txtViewAvg6.setText(df.format(avgHeights.get(3)));
            txtViewAvg6.setText(df.format(avgHeights.get(2)));
            txtViewAvg6.setText(df.format(avgHeights.get(1)));
            txtViewAvg6.setText(df.format(avgHeights.get(0)));
            avgHeights.add(5, 0.0);
            avgHeights.add(4, 0.0);
        } else if(avgHeights.size()== 3 ) {
            txtViewAvg6.setText(df.format(avgHeights.get(2)));
            txtViewAvg6.setText(df.format(avgHeights.get(1)));
            txtViewAvg6.setText(df.format(avgHeights.get(0)));
            avgHeights.add(5, 0.0);
            avgHeights.add(4, 0.0);
            avgHeights.add(3, 0.0);
        } else if(avgHeights.size() > 2) {
            txtViewAvg6.setText(df.format(avgHeights.get(1)));
            txtViewAvg6.setText(df.format(avgHeights.get(0)));
            avgHeights.add(5, 0.0);
            avgHeights.add(4, 0.0);
            avgHeights.add(3, 0.0);
            avgHeights.add(2, 0.0);
        } else if (avgHeights.size() == 1) {
            txtViewAvg6.setText(df.format(avgHeights.get(0)));
            avgHeights.add(1, 0.0);
            avgHeights.add(2, 0.0);
            avgHeights.add(3, 0.0);
            avgHeights.add(4, 0.0);
            avgHeights.add(5, 0.0);
        }

        avgHeightsList = avgHeights;

    }
    public ArrayList<Double> fillTable(PitchTest pitchTest){ // Filling table for all drops bar the averages
        ArrayList<TextView> tableCells = initializeTable();
        ArrayList<Double> heights = pitchTest.organiseHeights();

        for(int i = 0; i < heights.size() ; i++) {
            Double bounceHeight;
            try {
                bounceHeight = heights.get(i);
            }
            catch(IndexOutOfBoundsException e){
                bounceHeight = 0.0;
            }


            tableCells.get(i).setText(df.format(bounceHeight));

        }
        return heights; // To set as a variable to fill in the PDF and CSV files
    }

    public ArrayList<TextView> initializeTable(){
        /** Table text views **/
        // Location 1 drops or column 1
        TextView txtView1 = this.findViewById(R.id.textView10);
        TextView txtView2 = this.findViewById(R.id.textView18);
        TextView txtView3 = this.findViewById(R.id.textView26);
        TextView txtView4 = this.findViewById(R.id.textView34);
        TextView txtView5 = this.findViewById(R.id.textView42);

        // Location 2 drops or column 2
        TextView txtView6 = this.findViewById(R.id.textView11);
        TextView txtView7 = this.findViewById(R.id.textView19);
        TextView txtView8 = this.findViewById(R.id.textView27);
        TextView txtView9 = this.findViewById(R.id.textView35);
        TextView txtView10 = this.findViewById(R.id.textView43);


        // Location 3 drops or column 3
        TextView txtView11 = this.findViewById(R.id.textView12);
        TextView txtView12 = this.findViewById(R.id.textView20);
        TextView txtView13 = this.findViewById(R.id.textView28);
        TextView txtView14 = this.findViewById(R.id.textView36);
        TextView txtView15 = this.findViewById(R.id.textView44);



        // Location 4 drops or column 4
        TextView txtView16 = this.findViewById(R.id.textView13);
        TextView txtView17 = this.findViewById(R.id.textView21);
        TextView txtView18 = this.findViewById(R.id.textView29);
        TextView txtView19 = this.findViewById(R.id.textView37);
        TextView txtView20 = this.findViewById(R.id.textView45);



        // Location 5 drops or column 5
        TextView txtView21 = this.findViewById(R.id.textView14);
        TextView txtView22 = this.findViewById(R.id.textView22);
        TextView txtView23 = this.findViewById(R.id.textView30);
        TextView txtView24 = this.findViewById(R.id.textView38);
        TextView txtView25 = this.findViewById(R.id.textView46);



        // Location 6 drops or column 6
        TextView txtView26 = this.findViewById(R.id.textView15);
        TextView txtView27 = this.findViewById(R.id.textView23);
        TextView txtView28 = this.findViewById(R.id.textView31);
        TextView txtView29 = this.findViewById(R.id.textView39);
        TextView txtView30 = this.findViewById(R.id.textView47);
        tableDrops.add(0,txtView1);
        tableDrops.add(1,txtView2);
        tableDrops.add(2,txtView3);
        tableDrops.add(3,txtView4);
        tableDrops.add(4,txtView5);
        tableDrops.add(5,txtView6);
        tableDrops.add(6,txtView7);
        tableDrops.add(7,txtView8);
        tableDrops.add(8,txtView9);
        tableDrops.add(9,txtView10);
        tableDrops.add(10,txtView11);
        tableDrops.add(11,txtView12);
        tableDrops.add(12,txtView13);
        tableDrops.add(13,txtView14);
        tableDrops.add(14,txtView15);
        tableDrops.add(15,txtView16);
        tableDrops.add(16,txtView17);
        tableDrops.add(17,txtView18);
        tableDrops.add(18,txtView19);
        tableDrops.add(19,txtView20);
        tableDrops.add(20,txtView21);
        tableDrops.add(21,txtView22);
        tableDrops.add(22,txtView23);
        tableDrops.add(23,txtView24);
        tableDrops.add(24, txtView25);
        tableDrops.add(25,txtView26);
        tableDrops.add(26,txtView27);
        tableDrops.add(27,txtView28);
        tableDrops.add(28,txtView29);
        tableDrops.add(29,txtView30);
        return tableDrops;
    }

    public void generateCSV(PitchTest pitchTest) throws IOException { // Change this method to take PitchTest
        ArrayList<Double> allHeights  = pitchTest.organiseHeights();
        ArrayList<Double> avgHeights = pitchTest.getRunningAverages();
        Double TotalAverage = pitchTest.getTotalAvg();
        if (checkPermissionFromDevice()) {
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/CSV Files");

            boolean var = false;
            if (!folder.exists()) var = folder.mkdir();

            System.out.println("" + var);

            final String filename = folder.toString() + "/" + currentDate + "_" + currentTime
                    + ":" + Calendar.getInstance().get(Calendar.SECOND) + ".csv";

            try {
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


                for (Location loc : pitchTest.getLocations()){
                    int locationIndex = pitchTest.getLocations().indexOf(loc) + 1;
                    fw.append("Location" + locationIndex);
                    fw.append(",");
                    for(Result result : loc.getResults()){
                        Double bounceHeight = result.getBounceHeight();
                        fw.append(bounceHeight.toString());
                        fw.append(",");

                    }
                    Double avgBounceHeight = loc.getRunningAvg();
                    fw.append(avgBounceHeight.toString());
                    fw.append("\n");

                }

                fw.append("\n");
                fw.append("Average per Location");
                fw.append(",");
                for(Double avg : avgHeights) {
                    fw.append(avg.toString());
                    fw.append(",");
                }
                fw.append("\n");
                fw.append("Overall average");
                fw.append(pitchTest.getTotalAvg().toString());
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

                Toast.makeText(this, "Written to file \"" + folder.toString() + filename + "/\"", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        } else {
            requestPermission();
            this.finish();
            Toast.makeText(this, "Cannot create CSV without permission.", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateReportPDF(PitchTest pitchTest) throws IOException {

        Toast.makeText(FinalActivity.this, "PDF created.", Toast.LENGTH_SHORT).show();
        //create folder
        File folderPDF = new File(Environment.getExternalStorageDirectory() + "/PDF reports");

        ArrayList<Double> allHeights = pitchTest.organiseHeights();
//        ArrayList<Double> avgHeights = pitchTest.getRunningAverages();
        Double totalAvg = pitchTest.getTotalAvg();

        if (!folderPDF.exists()) folderPDF.mkdir();

        String filePDF = folderPDF.toString() + "/" + currentDate + "_" + currentTime
                + ":" + Calendar.getInstance().get(Calendar.SECOND) + ".pdf";

        //create fonts
        Font title = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
        //Font subtitle = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK));
        Font labelBlack = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
        Font labelRed = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.RED);
        Font other = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
        Intent intent = getIntent();
        PitchTest test = intent.getParcelableExtra(MapsActivity.TEST);
        LatLng coord = pitchTest.getLocation(0).getLocation();

        Log.i("PDF", "The number of heights is " + allHeights.size());
        if(allHeights.size() < 30){
            for(int i = allHeights.size() ; i < 30 ; i++){
                allHeights.add(i, 0.0); // fill up the heights if nothing is there
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


            insertCell(table, allHeights.get(0).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(5).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(10).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(15).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(20).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(25).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, allHeights.get(1).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(6).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(11).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(16).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(21).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(26).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, allHeights.get(2).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(7).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(12).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(17).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(22).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(27).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, allHeights.get(3).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(8).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(13).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(18).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(23).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(28).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);

            insertCell(table, allHeights.get(4).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(9).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(14).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(19).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(24).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);
            insertCell(table, allHeights.get(29).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.CYAN);


            insertCell(table, "Mean Result", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, avgHeightsList.get(0).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, avgHeightsList.get(1).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, avgHeightsList.get(2).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, avgHeightsList.get(3).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, avgHeightsList.get(4).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);
            insertCell(table, avgHeightsList.get(5).toString(), Element.ALIGN_CENTER, 1, 1, other, BaseColor.YELLOW);


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

