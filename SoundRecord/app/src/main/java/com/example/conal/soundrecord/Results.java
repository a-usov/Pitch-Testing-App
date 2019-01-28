package com.example.conal.soundrecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Results extends AppCompatActivity {

       private Button ok;
    private ImageView bad10;
    private ImageView bad20;
    private ImageView bad30;
    private ImageView bad40;
    private ImageView good45;
    private ImageView good50;
    private ImageView good55;
    private ImageView good60;
    private ImageView good65;
    private ImageView good70;
    private ImageView good75;
    private ImageView bad80;
    private ImageView bad90;
    private ImageView bad95;


    private int percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        onClickListenerButton();


    }

    public void onClickListenerButton(){
        ok = (Button) findViewById(R.id.buttonOK);
        bad10 = (ImageView) findViewById(R.id.bad10);
        bad20 = (ImageView) findViewById(R.id.bad20);
        bad30 = (ImageView) findViewById(R.id.bad30);
        bad40 = (ImageView) findViewById(R.id.bad40);
        good45 = (ImageView) findViewById(R.id.good45);
        good50 = (ImageView) findViewById(R.id.good50);
        good55 = (ImageView) findViewById(R.id.good55);
        good60 = (ImageView) findViewById(R.id.good60);
        good65 = (ImageView) findViewById(R.id.good65);
        good70 = (ImageView) findViewById(R.id.good70);
        good75 = (ImageView) findViewById(R.id.good75);
        bad80 = (ImageView) findViewById(R.id.bad80);
        bad90 = (ImageView) findViewById(R.id.bad90);
        bad95 = (ImageView) findViewById(R.id.bad95);
       // percent = findViewById(R.id.editText2);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bad10.setVisibility(View.INVISIBLE);
                bad20.setVisibility(View.INVISIBLE);
                bad30.setVisibility(View.INVISIBLE);
                bad40.setVisibility(View.INVISIBLE);
                good45.setVisibility(View.INVISIBLE);
                good50.setVisibility(View.INVISIBLE);
                good55.setVisibility(View.INVISIBLE);
                good60.setVisibility(View.INVISIBLE);
                good65.setVisibility(View.INVISIBLE);
                good70.setVisibility(View.INVISIBLE);
                good75.setVisibility(View.INVISIBLE);
                bad80.setVisibility(View.INVISIBLE);
                bad90.setVisibility(View.INVISIBLE);
                bad95.setVisibility(View.INVISIBLE);

                /*if ( ) {

                }*/
            }
            });

        }


    public void openHomePage(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void openMyTestsPage(){
        Intent intent = new Intent(this, MyTests.class);
        startActivity(intent);
    }

    //temporary way to get to the result page
    public void openResultsPage(){
        Intent intent = new Intent(this, Results.class);
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
        }else if(item.getItemId() == R.id.results) {
            openResultsPage();
        }else if(item.getItemId() == R.id.my_account){
            Toast.makeText(this, "This will be My Account page", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
