package com.example.conal.soundrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;
import static com.example.conal.soundrecord.MapsActivity.TEST;
import static com.example.conal.soundrecord.RecordingActivity.PATH;
import static com.example.conal.soundrecord.RecordingActivity.FOLDER;

public class ProcessingActivity extends AppCompatActivity {

    private Intent intent;
    private Result result;
    private PitchTest test;
    private AsyncTask<String, Void, Result> runner;
    public static double[] sound;

    public static final String SOUND = "com.example.conal.soundrecord.SOUND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        final ProgressBar spinner;
        spinner = findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        intent = getIntent();

        test = intent.getParcelableExtra(TEST);
        String path = intent.getStringExtra(PATH);

        new AndroidFFMPEGLocator(this);
        runner = new AsyncRunner().execute(path);

        // time is super long(5min), but timer cancels early whenever processing is done
        new CountDownTimer(500000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (runner.getStatus() == Status.FINISHED) {
                    afterProcessing();
                    this.cancel();
                }
            }

            // realistically never executes
            public void onFinish() {
                spinner.setVisibility(View.GONE);
                afterProcessing();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
    }

    private void afterProcessing() {
        File folderRecordings = (File) intent.getSerializableExtra(FOLDER);
        try {
            FileUtils.deleteDirectory(folderRecordings);
            Log.i("Processing", "Folder is deleted.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Processing", "No folder to delete.");
        }

        if (result != null) {
            test.getLocation(test.getNumDone()).addResult(result);

            Log.i("Processing", "bounceHeight: " + result.getBounceHeight());

            if (test.getLocation(test.getNumDone()).getNumDone() == 4) {
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("mapNeeded", true);
                editor.apply();
            }
            openResultsActivity();
        } else {
            Log.i("Processing", "bounceTime was 0");
            Toast.makeText(this, "Failed, try again", Toast.LENGTH_LONG).show();
            openRecordingActivity();
        }
    }


    private class AsyncRunner extends AsyncTask<String, Void, Result> {
        private SoundProcessing processor;

        protected Result doInBackground(String... strings) {
            Log.i("Recordings", "In Runner");
            return processor.process(strings[0]);
        }

        protected void onPreExecute() {
            processor = new SoundProcessing(44100, 2048);
        }

        protected void onPostExecute(Result returnResult) {
            if (returnResult != null) {
                sound = processor.soundArray;
                result = returnResult;
                result.setBounceHeight(1.23 * Math.pow((result.getTimeOfBounce() - 0.025), 2.0));
            }
        }
    }

    private void openResultsActivity() {
        intent = getIntent();

        intent.putExtra(TEST, test);

        intent.setClass(this, ResultsActivity.class);
        startActivity(intent);
    }

    private void openRecordingActivity() {
        intent = getIntent();

        intent.setClass(this, RecordingActivity.class);
        startActivity(intent);
    }

}
