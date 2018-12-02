package com.example.conal.soundrecord;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Processing extends AppCompatActivity {
    //Declare variables
    TextView mTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        mTextField = this.<TextView>findViewById(R.id.mTextField);

        final ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        spinner.setVisibility(View.VISIBLE);
        mTextField.setVisibility(View.VISIBLE);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");
                spinner.setVisibility(View.GONE);
                mTextField.setVisibility(View.GONE);
                openResultsActivity();
                }
        }.start();

       //
    }

    public void openResultsActivity(){
        Intent intent = new Intent(this, Results.class);
        startActivity(intent);
    }

}
