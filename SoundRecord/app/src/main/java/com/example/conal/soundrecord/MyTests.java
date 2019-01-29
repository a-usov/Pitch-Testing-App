package com.example.conal.soundrecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyTests extends AppCompatActivity {

    //Declare variables
    private TextView date1;
    private TextView date2;
    private TextView date3;
    private Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tests);

        date1 = (TextView) findViewById(R.id.date1);
        date2 = (TextView) findViewById(R.id.date2);
        date3 = (TextView) findViewById(R.id.date3);

        Intent intent = getIntent();
        loc = intent.getParcelableExtra(Processing.LOCATION);
        Toast.makeText(MyTests.this, loc.toString(), Toast.LENGTH_LONG).show();


        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrials();
            }
        });
        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrials();
            }
        });
        date3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrials();
            }
        });



    }

    public void openTrials(){
        Intent intent = new Intent(this, Trials.class);
        startActivity(intent);
    }
}
