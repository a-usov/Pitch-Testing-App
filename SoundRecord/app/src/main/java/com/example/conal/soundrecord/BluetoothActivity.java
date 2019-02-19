package com.example.conal.soundrecord;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private ArrayList<String[]> devices = new ArrayList<String[]>();
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Button btnBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        btnBluetooth = this.<Button>findViewById(R.id.btn_blu);

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {

            }
        });


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

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                //There are paired devices. Get name and address of each paired device
                for (BluetoothDevice device : pairedDevices) {
                    String[] dev = {device.getName(), device.getAddress()};

                    devices.add(dev);
                }
            }

            Toast.makeText(BluetoothActivity.this, devices.get(0)[1], Toast.LENGTH_LONG).show();
            //AT THIS POINT: got list of paired devices with names and MAC addresses -- String[] devices


            boolean success = mBluetoothAdapter.startDiscovery();

            if (success) {
                Toast.makeText(this,"Discovery started", Toast.LENGTH_LONG).show();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);
            }
            else Toast.makeText(this,"Discovery did not start", Toast.LENGTH_LONG).show();

            class AcceptThread extends Thread {
                private final BluetoothServerSocket mmServerSocket;

                public AcceptThread() {
                    // Use a temporary object that is later assigned to mmServerSocket
                    // because mmServerSocket is final.
                    BluetoothServerSocket tmp = null;
                    try {
                        // MY_UUID is the app's UUID string, also used by the client code.
                        tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(devices.get(0)[0], myUUID);
                    } catch (IOException e) {
                        Log.e("bluetooth", "Socket's listen() method failed", e);
                    }
                    mmServerSocket = tmp;
                }

                public void run() {
                    BluetoothSocket socket = null;
                    // Keep listening until exception occurs or a socket is returned.
                    while (true) {
                        try {
                            socket = mmServerSocket.accept();
                        } catch (IOException e) {
                            Log.e("bluetooth", "Socket's accept() method failed", e);
                            break;
                        }

                        if (socket != null) {
                            // A connection was accepted. Perform work associated with
                            // the connection in a separate thread.
                            manageMyConnectedSocket(socket);
                            try {
                                mmServerSocket.close();
                            }
                            catch(IOException i) {
                                Toast.makeText(BluetoothActivity.this,"Couldn't close socket.", Toast.LENGTH_LONG).show();
                            }
                            break;
                        }
                    }
                }

                // Closes the connect socket and causes the thread to finish.
                public void cancel() {
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        Log.e("bluetooth", "Could not close the connect socket", e);
                    }
                }
            }

        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    //...

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }



    private void manageMyConnectedSocket(BluetoothSocket socket) {

    }

    /*private OutputStream outputStream;
    private InputStream inStream;

    private void init() throws IOException {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if(bondedDevices.size() > 0) {
                    Object[] devices = (Object []) bondedDevices.toArray();
                    BluetoothDevice device = (BluetoothDevice) devices[0]; //was devices[position]
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                }

                Log.e("error", "No appropriate paired devices.");
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    public void run() {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;

        while (true) {
            try {
                bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
