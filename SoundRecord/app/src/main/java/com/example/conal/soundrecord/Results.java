package com.example.conal.soundrecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Results extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton result;
    private RadioButton good;
    private Button ok;
    private ImageView bar_good;
    private ImageView bar_bad;
    private TextView percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        onClickListenerButton();


    }

    public void onClickListenerButton(){
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ok = (Button) findViewById(R.id.buttonOK);
        good = (RadioButton) findViewById(R.id.radioButton);
        bar_good = (ImageView) findViewById(R.id.good);
        bar_bad = (ImageView) findViewById(R.id.bad);
        percent = (TextView) findViewById(R.id.percent);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                result = findViewById(selectedID);
                bar_good.setVisibility(View.INVISIBLE);
                bar_bad.setVisibility(View.INVISIBLE);

                if (result == good) {
                    bar_good.setVisibility(View.VISIBLE);
                    percent.setText("55%");
                } else {
                    bar_bad.setVisibility(View.VISIBLE);
                    percent.setText("30%");
                }
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
