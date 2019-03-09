package com.example.conal.soundrecord;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.ParcelUuid;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;

public class RecordingActivity extends AppCompatActivity {

    /** Recording variables **/
    final int REQUEST_PERMISSION_CODE = 1000;
    public static final String POSITION = "com.example.soundrecord.POSITION";
    public static final String FOLDER = "com.example.soundrecord.FOLDER";
    private static File folderPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordings");
    private static int numRecordings = 0;
    private Intent intent = new Intent();
    public static final String PATH = "com.example.soundRecord.PATH";
    //private static boolean bluetooth = false;
    BluetoothSocket socket;

    String pathSave = "";
    private WavRecorder recorder;
    ToggleButton btnRecording;

    private ArrayList<String[]> devices = new ArrayList<String[]>();
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    static final UUID myUUID = UUID.fromString("5004c681-e952-4df6-b361-0ccb2a3d1213");
    String mac = "98:D3:32:31:23:DA"; // MAC address of magnet drop


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        btnRecording = (ToggleButton) findViewById(R.id.btnRecording);

        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(this, "Bluetooth is supported", Toast.LENGTH_LONG).show();

            if (!mBluetoothAdapter.isEnabled()) {
                final int REQUEST_ENABLE_BT = 1;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

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

                                                            try {
                                                                //init();
                                                                write("1");
                                                                socket.close();
                                                            }
                                                            catch (IOException i) {
                                                                Toast.makeText(RecordingActivity.this, "oops", Toast.LENGTH_LONG).show();
                                                            }

                                                            startRecording();

                                                        } else {
                                                            // the toggle is disabled
                                                            stopRecording();
                                                        }
                                                    }
                                                }
        );
    }

    public void onResume() {
        super.onResume();

        if (mBluetoothAdapter.isEnabled()) {
            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            //boolean bluetooth = sharedpreferences.getBoolean("bluetooth", true);
            //if (!bluetooth) {
            try {
                BluetoothDevice device = (BluetoothDevice) mBluetoothAdapter.getRemoteDevice(mac);
                ParcelUuid[] uuids = device.getUuids();

                socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                if (!socket.isConnected()) {
                    socket.connect(); //Problem starts here
                    outputStream = socket.getOutputStream();
                }

                //SharedPreferences.Editor editor = sharedpreferences.edit();
                //editor.putBoolean("bluetooth", true);
            } catch (IOException i) {
                Toast.makeText(RecordingActivity.this, "Couldn't init", Toast.LENGTH_LONG).show();
            }
            //}

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                //There are paired devices. Get name and address of each paired device
                for (BluetoothDevice device : pairedDevices) {
                    String[] dev = {device.getName(), device.getAddress()};

                    devices.add(dev);
                }
            }
        }
    }

    private OutputStream outputStream;
    private InputStream inStream;

    private void init() throws IOException {
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        //Toast.makeText(this, bondedDevices.toString(), Toast.LENGTH_LONG).show(); //All paired devices

        //if(bondedDevices.size() > 0) {
        //Object[] devices = (Object []) bondedDevices.toArray();
        BluetoothDevice device = (BluetoothDevice) mBluetoothAdapter.getRemoteDevice(mac);
        ParcelUuid[] uuids = device.getUuids();
        //Toast.makeText(this, uuids[1].toString(), Toast.LENGTH_LONG).show();
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
        socket.connect();
        outputStream = socket.getOutputStream();
        //}

        //Log.e("error", "No appropriate paired devices.");
        //Toast.makeText(this, "No appropriate paired devices.", Toast.LENGTH_LONG).show();

    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
        //close here?

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
//                File folderPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordings");
            if (!folderPath.exists()) {
                folderPath.mkdir();
                Log.i("Recording", "Folder is created.");


            }

            String folderPath = RecordingActivity.folderPath.toString();

            // Save recording in folder
            pathSave = RecordingActivity.folderPath.getPath() + "/" + UUID.randomUUID().toString() + "_audio_record.wav";

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

        intent.putExtra(FOLDER, folderPath);
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