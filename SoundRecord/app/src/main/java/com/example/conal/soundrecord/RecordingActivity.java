package com.example.conal.soundrecord;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.ParcelUuid;
import android.support.v7.app.AlertDialog;
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

    private static File folderPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordings");
    private String filePath = "";
    private Intent intent;
    private WavRecorder recorder;
    private ToggleButton btnRecording;

    private BluetoothSocket socket;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private OutputStream outputStream;
    private boolean canUseBluetooth = false;
    private String nameDevice;

    public static final String FOLDER = "com.example.soundrecord.FOLDER";
    public static final String PATH = "com.example.soundRecord.PATH";
    public static final String DEVICE = "com.example.soundRecord.DEVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        btnRecording = findViewById(R.id.btnRecording);

        intent = getIntent();

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        nameDevice = sharedpreferences.getString(DEVICE, null);
        System.out.println(nameDevice);

        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                final int REQUEST_ENABLE_BT = 1;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

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
                    Log.i("sep", "Recording..");
                    if (canUseBluetooth) {
                        try {
                            //init();
                            write("1");
                            socket.close();
                        } catch (IOException i) {
                            Toast.makeText(RecordingActivity.this, "oops", Toast.LENGTH_LONG).show();
                        }
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
            final ArrayList<String> devices = new ArrayList<>();
            for (BluetoothDevice d : mBluetoothAdapter.getBondedDevices()) {
                devices.add(d.getName());
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Pick the drop device").setItems(devices.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nameDevice = devices.get(which);
                    init();
                }
            });

            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    nameDevice = "";
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(DEVICE, nameDevice);
                    editor.apply();
                }
            });

            if (nameDevice == null) {
                builder.show();
            }
        }
    }

    private void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    private void init() {
        BluetoothDevice device = null;

        for (BluetoothDevice d : mBluetoothAdapter.getBondedDevices()) {
            if (d.getName().equals(nameDevice)) {
                device = d;
            }
        }

        ParcelUuid[] uuids = device.getUuids();


        try {
            socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            if (!socket.isConnected()) {
                socket.connect(); //Problem starts here
                outputStream = socket.getOutputStream();
                canUseBluetooth = true;
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(DEVICE, nameDevice);
                editor.apply();
            }
        } catch (Exception e) {
            Toast.makeText(RecordingActivity.this, "Couldn't connect to " + device.getName(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
    }

    //Testing startRecording button instead of event listener
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
        intent.putExtra(DEVICE, nameDevice);
        intent.setClass(this, ProcessingActivity.class);

        startActivity(intent);
    }
}