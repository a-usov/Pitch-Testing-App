package com.example.conal.soundrecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Button footballBtn;
    private Button hockeyBtn;
    private Button rugbyBtn;
    private Button tennisBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Conall was here


        footballBtn = (Button) findViewById(R.id.footballBtn);
        hockeyBtn = (Button) findViewById(R.id.hockeyBtn);
        rugbyBtn = (Button) findViewById(R.id.rugbyBtn);
        tennisBtn = (Button) findViewById(R.id.tennisBtn);

        footballBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsPage();
            }
        });
        hockeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsPage();
            }
        });
        rugbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsPage();
            }
        });
        tennisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsPage();
            }
        });
    }

    public void openHomePage(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openMyTestsPage(){
        Intent intent = new Intent(this, MyTestsActivity.class);
        startActivity(intent);
    }


    //temporary way to get to the maps page, for football atm
    public void openMapsPage(){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

    //dropdown menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home) {
            openHomePage();
        }
        else if(item.getItemId() == R.id.my_tests){
            openMyTestsPage();
        }else if(item.getItemId() ==  R.id.footballBtn){ // In development of getting gps positions
            openMapsPage();
        }
        else{
            Toast.makeText(this, "This will be My Account page", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
