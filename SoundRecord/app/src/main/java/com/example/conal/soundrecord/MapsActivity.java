package com.example.conal.soundrecord;

import android.Manifest;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int REQUEST_PERMISSION_CODE = 1000;
    private GoogleMap gMap;
    private FusedLocationProviderClient client;
    private com.example.conal.soundrecord.Location location;
    private Intent intent;
    private PitchTest test;

    public static final String TEST = "com.example.soundrecord.TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = LocationServices.getFusedLocationProviderClient(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        intent = getIntent();

        if (intent.getParcelableExtra(TEST) != null) {
            test = intent.getParcelableExtra(TEST);
        } else {
            test = new PitchTest();
        }

        Button btnBegin = this.findViewById(R.id.btnBegin);
        btnBegin.setEnabled(true);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("mapNeeded", false);
        editor.apply();

        btnBegin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openRecordingPage();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // Get the current location
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request permissions.
            requestPermission();
        }
        client.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double longi = location.getLongitude();

                    LatLng cPos = new LatLng(lat, longi);
                    gMap.addMarker(new MarkerOptions().position(cPos).title("Current location."));

                    float maxZoomLevel = 18;
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cPos, maxZoomLevel));

                    MapsActivity.this.location = new com.example.conal.soundrecord.Location(cPos);
                    test.addLocation(MapsActivity.this.location);

                    Log.i("location", "Latitude " + lat);
                    Log.i("location", "Longitude" + longi);
                } else {
                    MapsActivity.this.location = new com.example.conal.soundrecord.Location();
                    test.addLocation(MapsActivity.this.location);
                    Log.i("location", "Location is null.");
                }
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

    public void openRecordingPage() {
        intent = getIntent();
        intent.setClass(this, RecordingActivity.class);
        intent.putExtra(TEST, test);
        startActivity(intent);
    }
}
