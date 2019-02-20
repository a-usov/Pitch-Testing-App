package com.example.conal.soundrecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    /** Recording variables **/
    final int REQUEST_PERMISSION_CODE = 1000;
    public static final String POSITION = "com.example.soundrecord.POSITION";
    public static final String FOLDER = "com.example.soundrecord.FOLDER";
    private static File folderRecordings = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordings");
    private static int numRecordings = 0;
    private Intent intent = new Intent();
    public static final String PATH = "com.example.soundRecord.PATH";

    String pathSave = "";
    private WavRecorder recorder;
    ToggleButton btnRecording;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        btnRecording = (ToggleButton) findViewById(R.id.btnRecording);

        if (!checkPermissionFromDevice()) requestPermission();



        /** Animation for recording button **/
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
                    Log.i("sep", "Recording..");
                    startRecording();


                } else {
                    // the toggle is disabled
                    stopRecording();
                }
            }


        }
        );
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
    // Permissions for recording
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
    public void startRecording() {
        Log.i("Recording", "Yay we're at recording.");
        if (checkPermissionFromDevice()) {
            Toast.makeText(this, "Recording...", Toast.LENGTH_SHORT).show();


            /** Making a folder to hold the 5 recordings **/
            Log.i("Recording", "Creating folder");
//                File folderRecordings = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordings");
            if (!folderRecordings.exists()) {
                folderRecordings.mkdir();
                Log.i("Recording", "Folder is created.");


            }

            String folderPath = folderRecordings.toString();

            // Save recording in folder
            pathSave = folderRecordings.getPath() + "/" + UUID.randomUUID().toString() + "_audio_record.wav";

            recorder = new WavRecorder(pathSave);
            Log.i("Recording", "Set up media recorder");

            Log.i("Recording", "Starting to record.");
            recorder.startRecording();

            btnRecording.setChecked(true);
        } else {
            requestPermission();
        }

    }

    public void stopRecording() {
        Log.i("Recording", "Stopping recording");
        recorder.stopRecording();
        btnRecording.setChecked(false);
        numRecordings++;

        intent.putExtra(FOLDER, folderRecordings);
        intent.setClass(this, ProcessingActivity.class);
        intent.putExtra(PATH, pathSave);

        openProcessingActivity(intent);
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





}