package com.example.conal.soundrecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyTests extends AppCompatActivity {

    private Button createPDF;
    private TextView date1;
    private TextView date2;
    private TextView date3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tests);

        date1 = (TextView) findViewById(R.id.date1);
        date2 = (TextView) findViewById(R.id.date2);
        date3 = (TextView) findViewById(R.id.date3);


        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrials();
            }
        });
        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrials();
            }
        });
        date3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrials();
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

    public void openTrials(){
        Intent intent = new Intent(this, Trials.class);
        startActivity(intent);
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

}
