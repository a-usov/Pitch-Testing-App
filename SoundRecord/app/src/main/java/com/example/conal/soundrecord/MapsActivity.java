package com.example.conal.soundrecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final int REQUEST_PERMISSION_CODE = 1000;
    //Declare variables
    Button btnStartRecord, btnStopRecord;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    int testCount = 0;
    private GoogleMap mMap;
    private double longit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (!checkPermissionFromDevice()) requestPermission();

		mapFragment.getMapAsync(this);

        btnStopRecord = this.<Button>findViewById(R.id.btnStopRecord);
        btnStartRecord = this.<Button>findViewById(R.id.btnStartRecord);

        btnStartRecord.setEnabled(true);
        btnStopRecord.setEnabled(false);


        btnStartRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (checkPermissionFromDevice()) {
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            UUID.randomUUID().toString() + "_audio_record.3_gpp";
                    setupMediaRecorder();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    btnStartRecord.setEnabled(false);
                    btnStopRecord.setEnabled(true);

                } else {
                    requestPermission();
                }
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaRecorder.stop();
                btnStartRecord.setEnabled(true);
                btnStopRecord.setEnabled(false);

                if (testCount == 1) {
                    openProcessingActivity();
                    testCount = 0;
                }
                testCount++;
            }
        });
        }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        Log.i("INFO", "Map is ready");
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        }, REQUEST_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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

    public void openProcessingActivity(){
        Intent intent = new Intent(this, Processing.class);
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

        if(item.getItemId() == R.id.home) {
            openHomePage();
        }
        else if(item.getItemId() == R.id.my_tests){
            openMyTestsPage();
        }else{
            Toast.makeText(this, "This will be My Account page", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
