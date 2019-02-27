package com.example.conal.soundrecord;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.UUID;

public class RecordingActivity extends AppCompatActivity {

    private static File folderPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordings");
    private String filePath = "";
    private WavRecorder recorder;
    private ToggleButton btnRecording;
    private Intent intent;

    public static final String FOLDER = "com.example.soundrecord.FOLDER";
    public static final String PATH = "com.example.soundRecord.PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        btnRecording = findViewById(R.id.btnRecording);

        // Animation for recording button
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        btnRecording.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                compoundButton.startAnimation(scaleAnimation);
                if (isChecked) {
                    // The toggle is enabled
                    Log.i("Recording", "Recording..");
                    startRecording();
                } else {
                    // the toggle is disabled
                    stopRecording();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    // Testing startRecording button instead of event listener
    private void startRecording() {
        Log.i("Recording", "Yay we're at recording.");
        Toast.makeText(this, "Recording...", Toast.LENGTH_SHORT).show();

        // Making a folder to hold the 5 recordings
        Log.i("Recording", "Creating folder");
        if (!folderPath.exists()) {
            folderPath.mkdir();
            Log.i("Recording", "Folder is created.");
        }

        // Save recording in folder
        filePath = folderPath.getPath() + "/" + UUID.randomUUID().toString() + "_audio_record.wav";

        recorder = new WavRecorder(filePath);
        recorder.startRecording();
        Log.i("Recording", "Starting to record.");

        btnRecording.setChecked(true);
    }

    private void stopRecording() {
        Log.i("Recording", "Stopping recording");
        recorder.stopRecording();
        btnRecording.setChecked(false);

        openProcessingActivity();
    }

    private void openProcessingActivity() {
        intent = getIntent();

        intent.putExtra(FOLDER, folderPath);
        intent.putExtra(PATH, filePath);
        intent.setClass(this, ProcessingActivity.class);

        startActivity(intent);
    }
}