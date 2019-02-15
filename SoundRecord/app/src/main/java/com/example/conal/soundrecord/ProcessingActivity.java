package com.example.conal.soundrecord;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;

import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;

public class ProcessingActivity extends AppCompatActivity {
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
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        spinner.setVisibility(View.VISIBLE);
        mTextField.setVisibility(View.VISIBLE);

        intent = getIntent();

        LatLng l = intent.getParcelableExtra(MapsActivity.POSITION);
        //Toast.makeText(this,"gi", Toast.LENGTH_SHORT).show();

        String path = intent.getStringExtra(MapsActivity.PATH);
        // TODO get rid of this
        Toast.makeText(this, "gotten path", Toast.LENGTH_SHORT).show();

        Location loc = new Location(l);

        /* TODO - Enable processing with new workflow, also test
        {

            new AndroidFFMPEGLocator(this);

            SoundProcessing processor = new SoundProcessing(44100, 2048);
            // TODO - Handle when -1 is returned, i.e. couldn't process properly
            double bounceTime = processor.process(path);
            double bounceHeight = 1.23 * Math.pow((bounceTime - 0.025), 2.0);

            loc.addHeight((float) bounceHeight);
        }
        */

        //Toast.makeText(this, l.toString() + " " + loc.getHeights(), Toast.LENGTH_SHORT).show();

        intent.setClass(this, ResultsActivity.class);
        intent.putExtra(LOCATION, loc);


        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + (millisUntilFinished + 1000) / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");
                spinner.setVisibility(View.GONE);
                mTextField.setVisibility(View.GONE);
                startActivity(intent);
            }
        }.start();

        File folderRecordings = (File) intent.getSerializableExtra(MapsActivity.FOLDER);

        try {
            FileUtils.deleteDirectory(folderRecordings);
            Log.i("Recordings", "Folder is deleted.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Recordings", "No folder to delete.");
        }
    }
}
