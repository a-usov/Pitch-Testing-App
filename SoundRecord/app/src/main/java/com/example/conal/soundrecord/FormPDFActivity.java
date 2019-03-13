package com.example.conal.soundrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.Calendar;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;

public class FormPDFActivity extends AppCompatActivity {

    private TextInputLayout jobNo;
    private TextInputLayout contract;
    private TextInputLayout surfaceName;
    private TextInputLayout airTemp;
    private TextInputLayout substrateType;
    private TextInputLayout testCondition;
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
    private TextInputLayout uncertaintyMeasurement;
    private TextInputLayout otherEquipment;

    // Radio buttons
    private Boolean fifaPro = false; // button for Fifa Pro spec or not
    private Boolean uk1 = false;
    private Boolean uk2 = false;
    private Boolean flight3 = false;
    private Boolean flight4 = false;
    private Boolean flight5 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pdf);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnSubmitPDF = this.findViewById(R.id.btnSubmitForm);
        btnSubmitPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPDF();

            }
        });

        jobNo = this.findViewById(R.id.job_no);
        contract = this.findViewById(R.id.contract);
        surfaceName = this.findViewById(R.id.surface_name);
        airTemp = this.findViewById(R.id.air_temp);
        surfaceTemp = this.findViewById(R.id.surface_temp);
        humidity = this.findViewById(R.id.humidity);
        windSpeed = this.findViewById(R.id.wind_speed);
        dayBook = this.findViewById(R.id.day_book);
        client = this.findViewById(R.id.client);
        dateOfConstruction = this.findViewById(R.id.construction_date);
        carpetType = this.findViewById(R.id.carpet_type);
        infillType = this.findViewById(R.id.infill_type);
        shockpad = this.findViewById(R.id.shockpad);
        weatherConditions = this.findViewById(R.id.weather_conditions);
        leadTechnician = this.findViewById(R.id.lead_technician);
        additionalTechnician = this.findViewById(R.id.additional_technician);
        substrateType = this.findViewById(R.id.substrate_type);
        testCondition = this.findViewById(R.id.test_conditions);
        uncertaintyMeasurement = this.findViewById(R.id.uncertainty_measurement);
        otherEquipment = this.findViewById(R.id.other);
    }

    public void onFifaRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.fifa:
                if (checked)
                    // Normal FIFA is best
                    Log.i("forms", "Normal fifa is best");
                fifaPro = false;
                break;
            case R.id.fifa_pro:
                if (checked)
                    // FIFA PRO all the way
                    Log.i("forms", "FIFA PRO!!");
                fifaPro = true;
                break;
        }
    }

    public void onEquipmentRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.uk1:
                if (checked)
                    // UK1 equipment
                    Log.i("forms", "UK1 equipment");
                uk1 = true;
                break;
            case R.id.uk2:
                if (checked)
                    // UK 2 equipment
                    // FIFA PRO all the way
                    Log.i("forms", "UK2 equipment");
                uk2 = true;
                break;
            case R.id.flight3:
                if (checked)
                    // Flight3
                    Log.i("forms", "Flight 3 ");
                flight3 = true;
                break;
            case R.id.flight4:
                if (checked)
                    // Flight 5 equipment
                    Log.i("forms", "Flight 4");
                flight4 = true;
                break;
            case R.id.flight5:
                if (checked)
                    // Flight 5
                    Log.i("forms", "Flight 5");
                flight5 = true;
                break;
        }
    }

    private void submitPDF() {
        // Create a shared preferences to store the user inputted data
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
        String time = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

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
        editor.putString("substrateType", substrateType.getEditText().getText().toString());
        editor.putString("testConditions", testCondition.getEditText().getText().toString());
        editor.putString("uncertaintyMeasurement", uncertaintyMeasurement.getEditText().getText().toString());
        editor.putString("other", otherEquipment.getEditText().getText().toString());
        editor.putBoolean("fifaPro", fifaPro);
        editor.putBoolean("uk1", uk1);
        editor.putBoolean("uk2", uk2);
        editor.putBoolean("flight3", flight3);
        editor.putBoolean("flight4", flight4);
        editor.putBoolean("flight5", flight5);
        editor.putString("currentDate", date);
        editor.putString("currentTime", time);

        // Save data
        editor.apply();
        Log.i("passing", "Job number saved.");
        openMapsActivity(); // Need location from offset so it will always go to this first
    }

    private void openMapsActivity() {
        Intent intent = getIntent();
        intent.setClass(this, MapsActivity.class);
        startActivity(intent);
    }
}
