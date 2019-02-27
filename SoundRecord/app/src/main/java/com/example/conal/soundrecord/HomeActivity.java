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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_CODE = 1000;

    public static final String MyPREFERENCES = "MyPrefs";
    public static String CONCRETETESTING = "concreteTesting";
    public static String MAPNEEDED = "mapNeeded"; // For global variable in sharedPrefs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(CONCRETETESTING, false);
        editor.putBoolean(MAPNEEDED, true);
        editor.apply();

        Button footballBtn = findViewById(R.id.footballBtn);
        Button hockeyBtn = findViewById(R.id.hockeyBtn);
        Button rugbyBtn = findViewById(R.id.rugbyBtn);
        Button tennisBtn = findViewById(R.id.tennisBtn);
        Button concreteBtn = findViewById(R.id.concreteBtn);

        concreteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(CONCRETETESTING, true);
                editor.apply();

                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openMapsPage();
                } else {
                    openMapsPage();
                }
            }
        });

        footballBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openFormPDF();
                } else {
                    openFormPDF();
                }
            }
        });

        hockeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openFormPDF();
                } else {
                    openFormPDF();
                }
            }
        });

        rugbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openFormPDF();
                } else {
                    openFormPDF();
                }
            }
        });

        tennisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissionFromDevice()) {
                    requestPermission();

                    if (checkPermissionFromDevice()) openFormPDF();
                } else {
                    openFormPDF();
                }
            }
        });
    }

    private void openHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void openMapsPage(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    //temporary way to get to the result page - TODO REMOVE
    private void openResultsPage() {
        Intent intent = new Intent(this, FinalActivity.class);
        startActivity(intent);
    }

    private void openFormPDF() {
        Intent intent = new Intent(this, FormPDFActivity.class);
        startActivity(intent);
    }

    private void openBluetoothPage() {
        Intent intent = new Intent(this, BluetoothActivity.class);
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

        if (item.getItemId() == R.id.home) {
            openHomePage();
        } else if (item.getItemId() == R.id.results) {
            openResultsPage();
        } else if (item.getItemId() == R.id.bluetooth) {
            openBluetoothPage();
        } else {
            Toast.makeText(this, "This will be My Test page", Toast.LENGTH_SHORT).show();
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
