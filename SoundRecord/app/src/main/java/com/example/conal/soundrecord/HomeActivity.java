package com.example.conal.soundrecord;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    final int REQUEST_PERMISSION_CODE = 1000;
    private Button footballBtn;
    private Button hockeyBtn;
    private Button rugbyBtn;
    private Button tennisBtn;
    Intent intent;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        intent = getIntent();
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
                intent.setClass(HomeActivity.this, FormPDFActivity.class);
                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openFormPDF();
                }
                else openFormPDF();
            }
        });

        hockeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecordingPage();
                /*intent.setClass(HomeActivity.this, FormPDFActivity.class);
                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openFormPDF();
                }
                else openFormPDF();*/
            }
        });

        rugbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomeActivity.this, FormPDFActivity.class);
                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openFormPDF();
                }
                else openFormPDF();
            }
        });

        tennisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HomeActivity.this, FormPDFActivity.class);
                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openFormPDF();
                }
                else openFormPDF();
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
        }, REQUEST_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    openFormPDF();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int access_coarse_location_result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED &&
                access_coarse_location_result == PackageManager.PERMISSION_GRANTED;
    }
}
