// AUTHORS - This code was written by Artem Usov, 2296905u@student.gla.ac.uk, Olga Jodelka 2266755K@student.gla.ac.uk
// Kara Newlands 2264457n@student.gla.ac.uk, Conall Clements 2197397c@student.gla.ac.uk
// It is licensed under the GNU General Public License version 3, 29 June 2007 (https://www.gnu.org/licenses/gpl-3.0.en.html)

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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_CODE = 1000;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String CONCRETETESTING = "concreteTesting";
    private static final String MAPNEEDED = "mapNeeded"; // For global variable in sharedPrefs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // set default configs
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

        // for every button, check permission and don't let user proceed without having all of them enabled
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


    private void openMapsPage(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void openFormPDF() {
        Intent intent = new Intent(this, FormPDFActivity.class);
        startActivity(intent);
    }
    
    // permission stuff
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
