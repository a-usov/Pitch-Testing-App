package com.example.conal.soundrecord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class Results extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton result;
    private RadioButton good;
    private SeekBar result_bar;
    private ImageView cross;
    private ImageView tick;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        onClickListenerButton();


    }

    public void onClickListenerButton(){
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        result_bar = (SeekBar) findViewById(R.id.seekBar);
        cross = (ImageView) findViewById(R.id.cross);
        tick = (ImageView) findViewById(R.id.tick);
        ok = (Button) findViewById(R.id.buttonOK);
        good = (RadioButton) findViewById(R.id.radioButton);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                result = findViewById(selectedID);
                tick.setVisibility(View.INVISIBLE);
                cross.setVisibility(View.INVISIBLE);

                if(result == good){
                    result_bar.setProgress(50);
                    tick.setVisibility(View.VISIBLE);
                } else{
                    result_bar.setProgress(15);
                    cross.setVisibility(View.VISIBLE);
                }
            }
        });


    }

}
