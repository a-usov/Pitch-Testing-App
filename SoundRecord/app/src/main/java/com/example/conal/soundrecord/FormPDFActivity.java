package com.example.conal.soundrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Set;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;

public class FormPDFActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private Button btnSubmitPDF;


     //    private String fields = "job_no,contract,test_condition,substrate_type,surface_name,air_temp,surface_temp,humidity,wind_speed,day_book,client,construction_date,carpet_type,infill_type,shockpad,weather_conditions,lead_technician,additional_technician,uncertainty_measurement";

    private TextInputLayout jobNo;
    private TextInputLayout contract;
    private TextInputLayout surfaceName;
    private TextInputLayout airTemp;
    private TextInputLayout surfaceTemp;
    private TextInputLayout humidity;
    private TextInputLayout windSpeed;
    private TextInputLayout dayBook;
    private TextInputLayout client;
    private TextInputLayout dateOfConstruction;
    private TextInputLayout carpetType;
    private TextInputLayout infillType;
    private TextInputLayout shockpad;
    private TextInputLayout weatherConditions;
    private TextInputLayout leadTechnician;
    private TextInputLayout additionalTechnician;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnSubmitPDF = this.<Button>findViewById(R.id.btnSubmitForm);

        jobNo = this.<TextInputLayout>findViewById(R.id.job_no);
        surfaceName = this.<TextInputLayout>findViewById(R.id.surface_name);
        airTemp = this.<TextInputLayout>findViewById(R.id.air_temp);
        surfaceTemp = this.<TextInputLayout>findViewById(R.id.surface_temp);
        humidity = this.<TextInputLayout>findViewById(R.id.humidity);
        windSpeed = this.<TextInputLayout>findViewById(R.id.wind_speed);
        dayBook = this.<TextInputLayout>findViewById(R.id.day_book);
        client = this.<TextInputLayout>findViewById(R.id.client);
        dateOfConstruction = this.<TextInputLayout>findViewById(R.id.construction_date);
        carpetType = this.<TextInputLayout>findViewById(R.id.carpet_type);
        infillType = this.<TextInputLayout>findViewById(R.id.infill_type);
        shockpad = this.<TextInputLayout>findViewById(R.id.shockpad);
        weatherConditions = this.<TextInputLayout>findViewById(R.id.weather_conditions);
        leadTechnician = this.<TextInputLayout>findViewById(R.id.lead_technician);
        additionalTechnician = this.<TextInputLayout>findViewById(R.id.additional_technician);




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
        /** Create a shared preferences to store the user inputted data **/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // Store data
        editor.putString("jobNo", jobNo.getEditText().getText().toString());
        editor.putString("contract", contract.getEditText().getText().toString());
        editor.putString("surfaceName", surfaceName.getEditText().getText().toString());
        editor.putString("humidity", humidity.getEditText().getText().toString());
        editor.putString("airTemp", airTemp.getEditText().getText().toString());
        editor.putString("surfaceTemp", surfaceTemp.getEditText().getText().toString());
        editor.putString("windSpeed", windSpeed.getEditText().getText().toString());
        editor.putString("dayBook", dayBook.getEditText().getText().toString());
        editor.putString("client", client.getEditText().getText().toString());
        editor.putString("dateOfConstruction", dateOfConstruction.getEditText().getText().toString());
        editor.putString("carpetTye", carpetType.getEditText().getText().toString());
        editor.putString("infillType", infillType.getEditText().getText().toString());
        editor.putString("shockpad", shockpad.getEditText().getText().toString());
        editor.putString("weatherConditions", weatherConditions.getEditText().getText().toString());
        editor.putString("leadTechnician", leadTechnician.getEditText().getText().toString());
        editor.putString("additionalTechnician", additionalTechnician.getEditText().getText().toString());

        // Save data
        editor.apply();
        Log.i("passing", "Job number saved.");
        openMapsActivity();

    }

    protected void openMapsActivity(){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}
