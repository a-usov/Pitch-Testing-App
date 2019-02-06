package com.example.conal.soundrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.conal.soundrecord.Home.MyPREFERENCES;

public class MyTests extends AppCompatActivity {

    private Button createPDF;
    private String currentDate;
    private String currentTime;
    private String jobNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tests);
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String dateKey = "currentDate";
        String dateTime = "currentTime";
        currentDate = sharedPref.getString("currentDate", "defaultStringIfNothingFound");
        currentTime = sharedPref.getString("currentTime", "defaultStringIfNothingFound");
        jobNo = sharedPref.getString("jobNo", "defaultStringIfNothingIsFound");
        Log.i("currentDate", "The current date is " + currentDate);
        Log.i("currentDate", "The time recorded is " + currentTime);

        createPDF = (Button) findViewById(R.id.btnPDF);



        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    generateReportPDF();

                } catch (IOException e){}
            }
        });

    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, int rowspan, Font font, BaseColor color ) {

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

    public void generateReportPDF() throws IOException {

        Toast.makeText(MyTests.this, "PDF created.", Toast.LENGTH_SHORT).show();
        //create folder
        File folderPDF = new File(Environment.getExternalStorageDirectory() + "/PDF reports");

        if(!folderPDF.exists()) folderPDF.mkdir();

        String filePDF = folderPDF.toString() + "/" + "Report.pdf";

        //create fonts
        Font title = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
        //Font subtitle = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK));
        Font labelBlack = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
        Font labelRed = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.RED);
        Font other = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
        Intent intent = getIntent();
        Location loc = intent.getParcelableExtra(Processing.LOCATION);
        LatLng coord = loc.getLocation();

        try{
            Document report = new Document();
            report.setPageSize(PageSize.A4);
            PdfWriter.getInstance(report, new FileOutputStream(filePDF));

            report.open();

            float [] pointColumnWidths = {600F, 600F, 600F, 600F, 600F, 600F, 600F};
            PdfPTable table = new PdfPTable(pointColumnWidths);

            // Adding cells to the table
            insertCell(table, "Determination of Ball Rebound (FIELD)\n" +
                    "FIFA Quality concept October 2015\n", Element.ALIGN_CENTER, 7, 1, title, BaseColor.WHITE);
            insertCell(table, "Job No", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, jobNo, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Contract", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Test Date", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, currentDate, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Test Condition", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Substrate Type", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Surface Name", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Time of day", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, currentTime, Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Air temp: (ºC)", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Surface temp: (ºC)", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Humidity: (%)", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Wind Speed: (m/s)", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            // insertCell(table, "", Element.ALIGN_LEFT, 2);
            insertCell(table, "", Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Day Book", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Client", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Date of Construction", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Carpet Type", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Infill Type", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Shockpad", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Weather Conditions", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Lead Technician", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, "Additional Technician", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);

            insertCell(table, "Uncertainty Measurement:", Element.ALIGN_LEFT, 2, 1, other, BaseColor.WHITE);
            insertCell(table, "", Element.ALIGN_LEFT, 1, 1, other, BaseColor.WHITE);
            insertCell(table, coord.toString(), Element.ALIGN_CENTER, 4, 1, other, BaseColor.WHITE);

            insertCell(table, "", Element.ALIGN_LEFT, 7, 1, labelBlack,BaseColor.BLACK);
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

            /*picture cell
            try {
                InputStream ims = getAssets().open("pitch.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scalePercent(30);
                image.setAlignment(Element.ALIGN_LEFT);
                PdfPCell imageCell = new PdfPCell(image);
                imageCell.setRowspan(4);
                imageCell.setColspan(7);
                table.addCell(imageCell);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            insertCell(table, "The picture of the pitch goes here", Element.ALIGN_CENTER, 7, 4, labelBlack, BaseColor.WHITE);
            insertCell(table, "FIFA Quality: 0.6 - 1.0m \t" + "FIFA Quality Pro: 0.6 - 0.85m", Element.ALIGN_CENTER, 7, 1, labelBlack, BaseColor.WHITE);

            insertCell(table, "UK 1", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.YELLOW);
            insertCell(table, "", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Flight 3", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.BLUE);
            insertCell(table, "", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "Flight 4", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.RED);
            insertCell(table, "", Element.ALIGN_CENTER, 2, 1, labelBlack, BaseColor.WHITE);
            insertCell(table, "UK 2", Element.ALIGN_CENTER, 1, 1,labelBlack, BaseColor.GREEN);
            insertCell(table, "", Element.ALIGN_CENTER, 1, 1,labelBlack, BaseColor.WHITE);
            insertCell(table, "Flight 5 (Norway)", Element.ALIGN_CENTER, 1, 1, labelBlack, BaseColor.CYAN);
            insertCell(table, "", Element.ALIGN_CENTER, 1, 1,labelBlack, BaseColor.WHITE);
            insertCell(table, "Other please state -", Element.ALIGN_CENTER, 1, 1,labelBlack, BaseColor.MAGENTA);
            insertCell(table, "", Element.ALIGN_CENTER, 2, 1,labelBlack, BaseColor.WHITE);

            report.add(table);

            report.close();

        }catch(Exception e){}
    }
    }

