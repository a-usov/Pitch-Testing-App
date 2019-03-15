package com.example.conal.soundrecord;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DecimalFormat;

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;
import static com.example.conal.soundrecord.HomeActivity.CONCRETETESTING;
import static com.example.conal.soundrecord.MapsActivity.TEST;
import static com.example.conal.soundrecord.RecordingActivity.DEVICE;

public class ResultsActivity extends AppCompatActivity {

    private boolean mapNeeded;
    private PitchTest test;
    private Intent intent;
    private Location loc;
    private boolean concreteTesting;
    private final DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // if we are doing concrete calibration, we show different results
        // compared to normal testing
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        concreteTesting = sharedpreferences.getBoolean(CONCRETETESTING, false);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        intent = getIntent();
        test = intent.getParcelableExtra(TEST);
        loc = test.getLocation(test.getNumDone());


        // SET TABLE
        final TextView height1 = findViewById(R.id.height1);
        final TextView height2 = findViewById(R.id.height2);
        final TextView height3 = findViewById(R.id.height3);
        final TextView height4 = findViewById(R.id.height4);
        final TextView height5 = findViewById(R.id.height5);
        final TextView average = findViewById(R.id.avg_H);

        double bounceHeight = 0.0;

        // fill out the table depending on how many bounces we've done
        // correct behaviour relies on fallthrough of cases
        switch (loc.getNumDone()) {
            case 4:
                bounceHeight = loc.getResults().get(4).getBounceHeight();
                // substring to get it to 2 decimal places
                height5.setText(df.format(bounceHeight));
            case 3:
                bounceHeight = loc.getResults().get(3).getBounceHeight();
                height4.setText(df.format(bounceHeight));
            case 2:
                bounceHeight = loc.getResults().get(2).getBounceHeight();
                height3.setText(df.format(bounceHeight));
            case 1:
                bounceHeight = loc.getResults().get(1).getBounceHeight();
                height2.setText(df.format(bounceHeight));
            case 0:
                bounceHeight = loc.getResults().get(0).getBounceHeight();
                height1.setText(df.format(bounceHeight));
        }

        average.setText(df.format(loc.getRunningAvg()));

        // SET SLIDER
        SeekBar sb = findViewById(R.id.seekBar);

        // Doesn't allow the user to change the progress but still displays the marker on the seekbar
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

        // if bounceheight is more than our maximum of 1.2m, set to maximum, else set location by convering from m to cm
        // we round as it has to be an integer
        if (loc.getResults().get(loc.getNumDone()).getBounceHeight() > 1.2) {
            sb.setProgress(120);
        } else {
            sb.setProgress((int) Math.round(loc.getResults().get(loc.getNumDone()).getBounceHeight() * 100));
        }

        // SET GRAPH
        GraphView graph = findViewById(R.id.graph);

        double[] sound = ProcessingActivity.sound;

        // we plot the line graph of the sound by making each value a point on the graph
        DataPoint[] points = new DataPoint[sound.length];
        for (int i = 0; i < sound.length; i++) {
            points[i] = new DataPoint(i, sound[i]);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);

        // we make the two points which are the peaks of where the two bounces are also points on the same graph
        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(loc.getResults().get(loc.getNumDone()).getFirstBounce(), sound[loc.getResults().get(loc.getNumDone()).getFirstBounce()]),
                new DataPoint(loc.getResults().get(loc.getNumDone()).getSecondBounce(), sound[loc.getResults().get(loc.getNumDone()).getSecondBounce()])
        });
        graph.addSeries(series2);
        series2.setShape(PointsGraphSeries.Shape.POINT);
        series2.setColor(Color.RED);
        series2.setSize(10);

        // we scale the graph so it all fits, get rid of x-axis labels
        // as they dont mean much
        graph.getViewport().setScalable(true);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);


        // SET BUTTONS
        Button btnNextDrop = this.findViewById(R.id.next_drop_btn);
        Button btnFinish = this.findViewById(R.id.finish_btn);
        Button btnRedo = this.findViewById(R.id.redo_btn);
        TableLayout tableLayout = this.findViewById(R.id.tableLayout2);
        ImageView slider = this.findViewById(R.id.imageView);

        // by default, we assume we are not concrete testing
        // so hide results button
        Button concreteResult = this.findViewById(R.id.concreteResultBtn);
        concreteResult.setVisibility(View.INVISIBLE);
        concreteResult.setEnabled(false);

        if (concreteTesting) {
            // if we are concrete testing, hide some stuff, display other stuff
            sb.setVisibility(View.INVISIBLE);
            slider.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
            btnNextDrop.setVisibility(View.INVISIBLE);
            concreteResult.setVisibility(View.VISIBLE);
            // here bounceheight is the very first bounce, which is where we always save the result to. We don't use the rest
            // of Pitchtest
            concreteResult.setText(df.format(bounceHeight) + " meters");
        }

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });

        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.getLocation(test.getNumDone()).deleteResult();
                openRecordingsPage();
            }
        });

        // if we're done with location, change text of next button to be more informative
        if (loc.getNumDone() == 4) btnNextDrop.setText("NEXT LOC");

        // depending on our progress in the test, next button takes us to different places. And we increment our counters
        btnNextDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                mapNeeded = sharedpreferences.getBoolean("mapNeeded", false);

                if (loc.getNumDone() == 4) {
                    test.incrementNumDone();
                } else {
                    test.increaseLocNumDone(test.getNumDone());
                }

                if (test.getNumDone() == 6) {
                    openFinalActivityPage();
                } else if (mapNeeded) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    openMapsActivity();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(false);
                    builder.setMessage("Have you moved to the next location").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                    //openMapsActivity();
                } else {
                    openRecordingsPage();
                }
            }
        });
    }

    private void goNext(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to finish?");

        builder.setCancelable(false);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (concreteTesting){
                    openHomePage();
                } else {
                    openFinalActivityPage();
                }
            }
        });

        builder.show();
    }

    // stop from going back
    @Override
    public void onBackPressed() {
    }

    private void openMapsActivity() {
        intent = getIntent();
        intent.setClass(this, MapsActivity.class);
        intent.putExtra(TEST, test);
        startActivity(intent);

    }

    private void openRecordingsPage() {
        intent = getIntent();
        intent.setClass(this, RecordingActivity.class);
        intent.putExtra(TEST, test);
        startActivity(intent);
    }

    private void openFinalActivityPage() {
        intent = getIntent();
        intent.setClass(this, FinalActivity.class);
        intent.putExtra(TEST, test);
        startActivity(intent);
    }

    // if we are concrete testing and we are done, remove TEST so when we start again, the old one is not used instead of making
    // a new one 
    private void openHomePage() {
        intent = getIntent();
        intent.setClass(this, HomeActivity.class);
        intent.removeExtra(TEST);
        intent.removeExtra(DEVICE);
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(DEVICE);
        editor.apply();
        startActivity(intent);
    }
}
