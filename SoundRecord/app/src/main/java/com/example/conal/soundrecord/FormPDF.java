package com.example.conal.soundrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static com.example.conal.soundrecord.Home.MyPREFERENCES;

public class FormPDF extends AppCompatActivity {
//    private int jobNo;
//    private String contract;
//    private String surfaceName;
//    private Float airtTemp;
//    private Float surfaceTemp;
//    private Float humidity;
//    private Float windSpeed;
//    private String dayBook;
//    private String client;
//    private Date dateOfConstruction;
//    private String carpetType;
//    private String infillType;
//    private String shockpad;
//    private String weatherConditions;
//    private String leadTechnician;
//    private String additionalTechnician;

    private SharedPreferences sharedpreferences;
    private Button btnSubmitPDF;


    private TextInputLayout jobNo;
    private Set<String> values;
//     job_no">Job Number</string>
//    contract">Contract</string>
//    test_condition">Test Condition</string>
//    ubstrate_type">Substrate Type</string>
//    surface_name">Surface Name</string>
//    air_temp">Air Temp</string>
//    surface_temp">Surface Temp</string>
//    humidity">Humidity</string>
//    wind_speed">Wind speed</string>
//    day_book">Day Book</string>
//    client">Client</string>
//    construction_date">Date of Construction</string>
//    carpet_type">Carpet Type</string>
//    infill_type">Infill Type</string>
//    shockpad">Shockpad</string>
//    weather_conditions">Weather Conditions</string>
//    lead_technician">Lead Technician</string>
//    additional_technician">Additional Technician</string>
//    uncertainty_measurement">Uncertainty Measurement</string>
//    private TextInputEditText contract;
//    private TextInputEditText surfaceName;
//    private TextInputEditText airTemp;
//    private TextInputEditText surfaceTemp;
//    private TextInputEditText humidity;
//    private TextInputEditText windSpeed;
//    private TextInputEditText dayBook;
//    private TextInputEditText client;
//    private TextInputEditText dateOfConstruction;
//    private TextInputEditText carpetType;
//    private TextInputEditText infillType;
//    private TextInputEditText shockpad;
//    private TextInputEditText weatherConditions;
//    private TextInputEditText leadTechnician;
//    private TextInputEditText additionalTechnician;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnSubmitPDF = this.<Button>findViewById(R.id.btnSubmitForm);

        jobNo = this.<TextInputLayout>findViewById(R.id.job_no);
//        contract = this.<TextInputEditText>findViewById(R.id.contract);
//        surfaceName = this.<TextInputEditText>findViewById(R.id.surface_name);
//        airTemp = this.<TextInputEditText>findViewById(R.id.air_temp);
//        surfaceTemp = this.<TextInputEditText>findViewById(R.id.surface_temp);
//        humidity = this.<TextInputEditText>findViewById(R.id.humidity);
//        windSpeed = this.<TextInputEditText>findViewById(R.id.wind_speed);
//        dayBook = this.<TextInputEditText>findViewById(R.id.day_book);
//        client = this.<TextInputEditText>findViewById(R.id.client);
//        dateOfConstruction = this.<TextInputEditText>findViewById(R.id.construction_date);
//        carpetType = this.<TextInputEditText>findViewById(R.id.carpet_type);
//        infillType = this.<TextInputEditText>findViewById(R.id.infill_type);
//        shockpad = this.<TextInputEditText>findViewById(R.id.shockpad);
//        weatherConditions = this.<TextInputEditText>findViewById(R.id.weather_conditions);
//        leadTechnician = this.<TextInputEditText>findViewById(R.id.lead_technician);
//        additionalTechnician = this.<TextInputEditText>findViewById(R.id.additional_technician);

        btnSubmitPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPDF();

            }
        });






//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });



    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    protected void submitPDF(){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        
        editor.putString("jobNo", jobNo.getEditText().getText().toString());
        editor.apply();
        Log.i("passing", "Job number saved.");
        openMapsActivity();

    }

    protected void openMapsActivity(){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}
