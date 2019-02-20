package com.example.conal.soundrecord;

<<<<<<< HEAD
import android.content.Intent;
=======
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
>>>>>>> 5e0a44e639045b3089a1deceff3029f05d8ed633
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    Button btnNextDrop, btnFinish, btnRedo;
    SharedPreferences sharedPreferences;
    private boolean mapNeeded;

    private SeekBar sb;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView height1 = (TextView) findViewById(R.id.height1);


        intent = getIntent();
        Location loc = intent.getParcelableExtra(ProcessingActivity.LOCATION);

        btnNextDrop = this.findViewById(R.id.next_drop_btn);
        btnFinish = this.findViewById(R.id.finish_btn); // End the test early
        btnRedo = this.findViewById(R.id.redo_btn);


        btnNextDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                mapNeeded = sharedpreferences.getBoolean("mapNeeded", false);

                if (mapNeeded) {
                    openMapsActivity();
                }

                else{
                    openRecordingsPage();
                }

            }
        });
    }

        public void openMapsActivity(){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

    }

    public void openRecordingsPage(){
        Intent intent = new Intent(this,RecordingActivity.class);
        startActivity(intent);


        sb = (SeekBar) findViewById(R.id.seekBar);

        /** Doesn't allow the user to change the progress but still displays the marker on the seekbar**/
        sb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0; // Amount of progress changed

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress; // Used to set progress from both a user and the program
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("seekbar", "Progress is " + progressChanged);
            }
        });

        sb.setProgress(50);

        height1.setText(loc.getHeights().get(0).toString().substring(0, 4));

//

    }


}
