package com.example.conal.soundrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import static com.example.conal.soundrecord.HomeActivity.MyPREFERENCES;
import static com.example.conal.soundrecord.HomeActivity.CONCRETETESTING;
import static com.example.conal.soundrecord.MapsActivity.TEST;

public class ResultsActivity extends AppCompatActivity {

    private boolean mapNeeded;
    private PitchTest test;
    private Intent intent;
    private Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean concreteTesting = sharedpreferences.getBoolean(CONCRETETESTING, false);

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

        Double bounceHeight = 0.0;

        switch (loc.getNumDone()) {
            case 4:
                bounceHeight = loc.getResults().get(4).getBounceHeight();
                height5.setText(bounceHeight.toString().substring(0, 4));
            case 3:
                bounceHeight = loc.getResults().get(3).getBounceHeight();
                height4.setText(bounceHeight.toString().substring(0, 4));
            case 2:
                bounceHeight = loc.getResults().get(2).getBounceHeight();
                height3.setText(bounceHeight.toString().substring(0, 4));
            case 1:
                bounceHeight = loc.getResults().get(1).getBounceHeight();
                height2.setText(bounceHeight.toString().substring(0, 4));
            case 0:
                bounceHeight = loc.getResults().get(0).getBounceHeight();
                height1.setText(bounceHeight.toString().substring(0, 4));
        }

        average.setText(loc.getRunningAvg().toString().substring(0, 4));

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

        if (loc.getResults().get(loc.getNumDone()).getBounceHeight() > 1.2) {
            sb.setProgress(120);
        } else {
            sb.setProgress((int) Math.round(loc.getResults().get(loc.getNumDone()).getBounceHeight() * 100));
        }

        // SET GRAPH
        GraphView graph = findViewById(R.id.graph);

        double[] sound = ProcessingActivity.sound;

        DataPoint[] points = new DataPoint[sound.length];
        for (int i = 0; i < sound.length; i++) {
            points[i] = new DataPoint(i, sound[i]);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);

        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(loc.getResults().get(loc.getNumDone()).getFirstBounce(), sound[loc.getResults().get(loc.getNumDone()).getFirstBounce()]),
                new DataPoint(loc.getResults().get(loc.getNumDone()).getSecondBounce(), sound[loc.getResults().get(loc.getNumDone()).getSecondBounce()])
        });
        graph.addSeries(series2);
        series2.setShape(PointsGraphSeries.Shape.POINT);
        series2.setColor(Color.RED);
        series2.setSize(10);

        graph.getViewport().setScalable(true);

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);


        // SET BUTTONS
        Button btnNextDrop = this.findViewById(R.id.next_drop_btn);
        Button btnFinish = this.findViewById(R.id.finish_btn);
        Button btnRedo = this.findViewById(R.id.redo_btn);
        TableLayout tableLayout = this.findViewById(R.id.tableLayout2);
        ImageView slider = this.findViewById(R.id.imageView);

        Button concreteResult = this.findViewById(R.id.concreteResultBtn);
        concreteResult.setVisibility(View.INVISIBLE);
        concreteResult.setEnabled(false);

        if (concreteTesting) {
            sb.setVisibility(View.INVISIBLE);
            slider.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
            btnNextDrop.setVisibility(View.INVISIBLE);
            concreteResult.setVisibility(View.VISIBLE);
            concreteResult.setText(bounceHeight.toString().substring(0, 4) + " meters");


            // I think it should go to home, saving csv/pdf might be funny if not gone to pdf activity
            btnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openHomePage();
                }
            });
        } else {
            btnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFinalActivityPage();
                }
            });
        }

        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.getLocation(test.getNumDone()).deleteResult();
                openRecordingsPage();
            }
        });

        if (loc.getNumDone() == 4) btnNextDrop.setText("NEXT LOC");

        btnNextDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                mapNeeded = sharedpreferences.getBoolean("mapNeeded", false);

                if (loc.getNumDone() == 4) {
                    test.incrementNumDone();
                } else {
                    test.increaseLocNumDone(test.getNumDone());
                }

                if (test.getNumDone() == 5) {
                    openFinalActivityPage();
                } else if (mapNeeded) {
                    openMapsActivity();
                } else {
                    openRecordingsPage();
                }
            }
        });
    }

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

    private void openHomePage() {
        intent = getIntent();
        intent.setClass(this, HomeActivity.class);
        intent.removeExtra(TEST);
        startActivity(intent);
    }
}
