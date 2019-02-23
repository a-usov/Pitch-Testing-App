package com.example.conal.soundrecord;

import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

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
        Double bounceHeight = loc.getResults().get(0).bounceHeight;
        height1.setText(bounceHeight.toString().substring(0, 4));

        btnNextDrop = this.findViewById(R.id.next_drop_btn);
        btnFinish = this.findViewById(R.id.finish_btn); // End the test early
        btnRedo = this.findViewById(R.id.redo_btn);

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

        double[] sound = intent.getDoubleArrayExtra(ProcessingActivity.SOUND);


        DataPoint[] points = new DataPoint[sound.length];
        for (int i = 0; i < sound.length; i++) {
            points[i] = new DataPoint(i, sound[i]);
        }


        GraphView graph = findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);

        // Weird stuff where scaling of graph doesnt work properly if you try to do both points at the same time
        // So do 2 point graphs, adding second point first so scales properly
        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(loc.getResults().get(0).firstBounce, sound[loc.getResults().get(0).firstBounce]),
                new DataPoint(loc.getResults().get(0).secondBounce, sound[loc.getResults().get(0).secondBounce])
        });
        graph.addSeries(series2);
        series2.setShape(PointsGraphSeries.Shape.POINT);
        series2.setColor(Color.RED);
        series2.setSize(10);

        graph.getViewport().setScalable(true);

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);


        btnNextDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                mapNeeded = sharedpreferences.getBoolean("mapNeeded", false);

                if (mapNeeded) {
                    openMapsActivity();
                } else {
                    openRecordingsPage();
                }

            }
        });
    }

    public void openMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    public void openRecordingsPage() {
        Intent intent = new Intent(this, RecordingActivity.class);
        startActivity(intent);
    }


}
