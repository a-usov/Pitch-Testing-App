package com.example.conal.soundrecord;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Environment;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final int REQUEST_PERMISSION_CODE = 1000;
    public static final String POSITION = "com.example.soundrecord.POSITION";
    public static final String FOLDER = "com.example.soundrecord.FOLDER";
    //Declare variables
    Button btnBegin;
    TextView txtLocation;
    String pathSave = "";
    private WavRecorder recorder;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    LatLng cPos;
    com.example.conal.soundrecord.Location loc;
    private double lat;
    private double longi;
    private Intent intent = new Intent();
    public static final String PATH = "com.example.soundRecord.PATH";

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = LocationServices.getFusedLocationProviderClient(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        btnBegin = this.<Button>findViewById(R.id.btnBegin);

        btnBegin.setEnabled(true);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();


        btnBegin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    //LatLng latlng = new LatLng();
                    loc = new com.example.conal.soundrecord.Location(cPos);

                }
                catch(NullPointerException n) {
                    Toast.makeText(MapsActivity.this, "No LatLng", Toast.LENGTH_LONG).show();
                }
                editor.putBoolean("mapNeeded", false);
                editor.apply();
                Intent intent = getIntent();
                intent.setClass(MapsActivity.this, RecordingActivity.class); //This class needs created
                intent.putExtra(POSITION,loc);
                openRecordingPage();
            }
        });



        if (!checkPermissionFromDevice()) requestPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        // Get the current location
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request permissions.
            requestPermission();
        }
        client.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.i("yada", "Location: " + location.toString());

                    lat = location.getLatitude();
                    longi = location.getLongitude();

                    cPos = new LatLng(lat, longi);
                    mMap.addMarker(new MarkerOptions().position(cPos).title("Current location."));
                    float maxZoomLevel = 18;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cPos, maxZoomLevel));
                    Log.i("location", "Latitude " + lat);
                    Log.i("location", "Longitude" + longi);
                    intent.putExtra(POSITION, cPos);



                } else {
                    Log.i("location", "Location is null.");
                }

            }
        });


        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecordingPage();
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
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
        int access_coarse_location_result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return access_coarse_location_result == PackageManager.PERMISSION_GRANTED;
    }

    public void openProcessingActivity(Intent intent) {
        //Intent intent = new Intent(this, ProcessingActivity.class);
        startActivity(intent);
    }

    public void openHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openMyTestsPage() {
        Intent intent = new Intent(this, FinalActivity.class);
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

        if (item.getItemId() == R.id.home) {
            openHomePage();
        } else if (item.getItemId() == R.id.my_tests) {
            openMyTestsPage();
        } else {
            Toast.makeText(this, "This will be My Account page", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
