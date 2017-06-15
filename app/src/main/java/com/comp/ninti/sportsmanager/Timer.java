package com.comp.ninti.sportsmanager;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Timer extends AppCompatActivity implements View.OnClickListener {

    Chronometer mChronometer;
    Button btnStart, btnStop, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_timer);


        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        btnStart = (Button) findViewById(R.id.start);
        btnStop = (Button) findViewById(R.id.stop);
        setBtnDisabled(btnStop);
        btnReset = (Button) findViewById(R.id.reset);
        setBtnDisabled(btnReset);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            switchButton(btnStart);
            switchButton(btnStop);
        } else if (v.getId() == R.id.stop) {
            mChronometer.stop();
            switchButton(btnStop);
            switchButton(btnReset);
            long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        } else if (v.getId() == R.id.reset) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            switchButton(btnReset);
            switchButton(btnStart);

            //mChronometer.setFormat("Formatted time (%s)");
            //mChronometer.setFormat(null);
        }
    }

    private void switchButton(Button button) {
        if (button.getVisibility() == View.VISIBLE) {
            setBtnDisabled(button);
        } else {
            setBtnEnabled(button);
        }
    }

    private void setBtnDisabled(Button button){
        button.setVisibility(View.INVISIBLE);
        button.setEnabled(false);
        button.setClickable(false);
    }

    private void setBtnEnabled(Button button){
        button.setVisibility(View.VISIBLE);
        button.setEnabled(true);
        button.setClickable(true);
    }
}


