package com.example.conal.soundrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private Button footballBtn;
    private Button hockeyBtn;
    private Button rugbyBtn;
    private Button tennisBtn;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        //Conall was here
//        DateFormat currentTime = Calendar.getInstance().
//        Log.i("getDate", "The current date is " + currentTime.toString());

        Date today;
        Date currentTime;
        String timeOutput;
        String output;
        SimpleDateFormat formatter;
        SimpleDateFormat timeFormatter;
        String pattern = "dd.MM.yy";
        String timePattern = "h:mm a";
        Locale currentLocale = Locale.UK;
        formatter = new SimpleDateFormat(pattern,currentLocale);
        timeFormatter = new SimpleDateFormat(timePattern, currentLocale);
        currentTime = new Date();
        today = new Date();
        output = formatter.format(today);
        timeOutput = timeFormatter.format(currentTime);
        Log.i("getDate",pattern + " " + output);
        Log.i("getDate", timePattern + " " + timeOutput);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("currentDate",output);
        editor.putString("currentTime", timeOutput);
        editor.apply();
        Log.i("passing", "Date and time saved.");




        footballBtn = (Button) findViewById(R.id.footballBtn);
        hockeyBtn = (Button) findViewById(R.id.hockeyBtn);
        rugbyBtn = (Button) findViewById(R.id.rugbyBtn);
        tennisBtn = (Button) findViewById(R.id.tennisBtn);

        footballBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFormPDF();
            }
        });
        hockeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecordingPage();
            }
        });
        rugbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsPage();
            }
        });
        tennisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsPage();
            }
        });

    }

    public void openHomePage(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openResultsPage(){
        Intent intent = new Intent(this, FinalActivity.class);
        startActivity(intent);
    }

    //temporary way to get to the maps page, for football atm
    public void openMapsPage(){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

    public void openFormPDF(){
        Intent intent = new Intent(this, FormPDFActivity.class);
        startActivity(intent);
    }

    public void openRecordingPage(){
        Intent intent = new Intent(this, RecordingActivity.class);
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

        if(item.getItemId() == R.id.home) {
            openHomePage();
        }
        else if(item.getItemId() == R.id.my_tests){
            openResultsPage();
        }else if(item.getItemId() ==  R.id.footballBtn){ // In development of getting gps positions
            openMapsPage();
        }
        else if(item.getItemId() == R.id.results) {
            openResultsPage();
        }else if(item.getItemId() == R.id.formPDF) {
                openFormPDF();
        }else{
            Toast.makeText(this, "This will be My Account page", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
