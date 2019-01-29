package com.example.conal.soundrecord;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final int REQUEST_PERMISSION_CODE = 1000;
    public static final String POSITION = "com.example.soundrecord.POSITION";
    //Declare variables
    Button btnStartRecord, btnStopRecord, btnGetLocation;
    TextView txtLocation;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private double lat;
    private double longi;
    private static int numRecordings = 0;
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = LocationServices.getFusedLocationProviderClient(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnStopRecord = this.<Button>findViewById(R.id.btnStopRecord);
        btnStartRecord = this.<Button>findViewById(R.id.btnStartRecord);



        btnStartRecord.setEnabled(true);
        btnStopRecord.setEnabled(false);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        /** testing running multiple tests **/
//        for(int tests = 0; tests < totalTests ; tests++){
//            // !!!!! You need this for the map to display the marker
//            mapFragment.getMapAsync(this);
//
//        }
        mapFragment.getMapAsync(this);


        if (!checkPermissionFromDevice()) requestPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        // Get the current location
        if(ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED ){
            // If permission is not granted, request permissions.
            requestPermission();
        }
        client.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Log.i("yada", "Location: " + location.toString());
//                    txtLocation.setText(location.toString());
                    lat = location.getLatitude();
                    longi = location.getLongitude();
                    // Update current location
                    // Add a marker in Sydney and move the camera
//              Log.i("yada", "Map is ready");
//               Toast.makeText(this, "Map is ready", Toast.LENGTH_LONG).show();
                    LatLng cPos = new LatLng(lat, longi);
                    mMap.addMarker(new MarkerOptions().position(cPos).title("Current location."));
                    float maxZoomLevel = mMap.getMaxZoomLevel();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cPos,maxZoomLevel));
                    Log.i("location", "Latitude " + lat);
                    Log.i("location", "Longitude" + longi);
                    // Ready /update map
                    //onMapReady(mMap);

                    //intent = getIntent();
                    intent.putExtra(POSITION, cPos);



                }
                else{
                    Log.i("location", "Location is null.");
                }

            }
        });


    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
        Log.i("Recording", "setting up Media Recoder is done ");

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

    /*
 Testing startRecording button instead of event listener
  */
    public void startRecording(View view){
        Log.i("Recording", "Yay we're at recording.");
        if (checkPermissionFromDevice()) {
            Log.i("Recording", "Permission is fine");
            Toast.makeText(this, "Recording...", Toast.LENGTH_SHORT).show();
            pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    UUID.randomUUID().toString() + "_audio_record.3_gpp";
            setupMediaRecorder();
            Log.i("Recording", "Set up media recorder");

            try {
                Log.i("Recording", "Preparing to record.");
                mediaRecorder.prepare();
                Log.i("Recording", "Starting to record.");
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

    public void stopRecording(View view){
                Log.i("Recording", "Stopping recording");
                mediaRecorder.stop();
                btnStartRecord.setEnabled(true);
                btnStopRecord.setEnabled(false);
                numRecordings++;

                //Intent intent = new Intent(view.getContext(), Processing.class);
                intent.setClass(this, Processing.class);

                //Code below ensures enough bounces (change 2 to 6 in final version)
                if (numRecordings > 2) openProcessingActivity(intent);
                else {
                    intent.setClass(view.getContext(), MapsActivity.class);
                    startActivity(intent);
                }
    }

    public void openProcessingActivity(Intent intent){
        //Intent intent = new Intent(this, Processing.class);
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
