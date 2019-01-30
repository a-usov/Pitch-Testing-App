package com.example.conal.soundrecord;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Processing extends AppCompatActivity {
    //Declare variables
    TextView mTextField;
    public static final String LOCATION = "com.example.conal.soundrecord.LOCATION";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        mTextField = this.<TextView>findViewById(R.id.mTextField);

        final ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        spinner.setVisibility(View.VISIBLE);
        mTextField.setVisibility(View.VISIBLE);

        intent = getIntent();

        LatLng l = intent.getParcelableExtra(MapsActivity.POSITION);
        //Toast.makeText(this,"gi", Toast.LENGTH_SHORT).show();

        Location loc = new Location(l);

        // DO PROCESSING HERE
        for(int i = 0; i < 5; i++) {
            Random rand = new Random();
            int n = rand.nextInt(10) + 60; //Gets a "random" bounce between 60 and 70 (from our own bounce tests)
            loc.addHeight((float)n);
        }

        Toast.makeText(this, l.toString() + " " + loc.getHeights(), Toast.LENGTH_SHORT).show();

        intent.setClass(this, MyTests.class);
        intent.putExtra(LOCATION, loc);


        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + (millisUntilFinished + 1000) / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");
                spinner.setVisibility(View.GONE);
                mTextField.setVisibility(View.GONE);
                openResultsActivity(intent);
                }
        }.start();

        File folderRecordings = (File)intent.getSerializableExtra(MapsActivity.FOLDER);

        try {
            FileUtils.deleteDirectory(folderRecordings);
            Log.i("Recordings", "Folder is deleted.");
        }
        catch (IOException e){
            e.printStackTrace();
            Log.i("Recordings", "No folder to delete.");
        }
    }

    public void openResultsActivity(Intent intent){
        //Intent intent = new Intent(this, Results.class);
        startActivity(intent);
    }

}
