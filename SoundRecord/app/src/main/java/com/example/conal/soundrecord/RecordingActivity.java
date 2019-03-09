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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

        intent = getIntent();
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        nameDevice = sharedpreferences.getString(DEVICE, null);

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

        Button btnReconnect = findViewById(R.id.btnReconnect);

        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameDevice = null;
                tryConnect();
            }
        });

        // Animation for recording button
        btnRecording = findViewById(R.id.btnRecording);

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
                    if (canUseBluetooth) {
                        try {
                            //init();
                            write("1");
                            socket.close();
                        } catch (IOException i) {
                            Toast.makeText(RecordingActivity.this, "oops", Toast.LENGTH_LONG).show();
                        }
                    }
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
        tryConnect();
    }

    private void tryConnect(){
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

            builder.setCancelable(false);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });

            builder.setNegativeButton("None", new DialogInterface.OnClickListener() {
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
            } else if (!nameDevice.equals("")){
                init();
            }
        }
    }

    private void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    private void init() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        BluetoothDevice device = null;

        for (BluetoothDevice d : mBluetoothAdapter.getBondedDevices()) {
            if (d.getName().equals(nameDevice)) {
                device = d;
            }
        }

        try {
            ParcelUuid[] uuids = device.getUuids();

            socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            if (!socket.isConnected()) {
                socket.connect();

                // at this point we know connection is made
                outputStream = socket.getOutputStream();
                canUseBluetooth = true;
                editor.putString(DEVICE, nameDevice);
                editor.apply();
                Toast.makeText(RecordingActivity.this, "Connected to " + device.getName(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            editor.remove(DEVICE);
            editor.apply();
            nameDevice = null;
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
        intent.setClass(this, ProcessingActivity.class);

        startActivity(intent);
    }
}