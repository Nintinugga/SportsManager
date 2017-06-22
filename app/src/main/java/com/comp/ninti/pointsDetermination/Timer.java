package com.comp.ninti.pointsDetermination;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.ninti.general.TimeUtil;
import com.comp.ninti.general.core.Event;
import com.comp.ninti.general.core.Rule;
import com.comp.ninti.sportsmanager.R;

import java.util.Calendar;


public class Timer extends AppCompatActivity implements View.OnClickListener {

    private Chronometer mChronometer;
    private Button timerBtn;
    private ImageButton addPenaltyTime;
    private long addedPenaltyTime = 0;
    long elapsedMillis = 0;
    long pointOfStop = 0;
    private Event event;
    private Rule rule;
    private String customerName,attempt;
    private TextView tvDiscTitle;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset) {
            refresh();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Event");
        rule = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Rule");
        customerName = getIntent().getStringExtra("CUSTOMER");
        attempt = getIntent().getStringExtra("ATTEMPT");
        Intent intent = new Intent();
        intent.putExtra("com.comp.ninti.general.core.Event", event);
        setResult(RESULT_CANCELED, intent);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(customerName);
        getSupportActionBar().setSubtitle(attempt);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        addPenaltyTime = (ImageButton) findViewById(R.id.addPenaltyTime);

        tvDiscTitle = (TextView) findViewById(R.id.discTitle);
        tvDiscTitle.setText(getIntent().getStringExtra("DISCIPLINE"));
        addPenaltyTime.setTag(0);
        timerBtn = (Button) findViewById(R.id.timerBtn);
        timerBtn.setTag(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.timerBtn) {
            final int status = (Integer) v.getTag();
            if (status == 0) {
                //start chronometer
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                timerBtn.setText(R.string.chronometer_stop);
                timerBtn.setTag(1);
            } else if (status == 1) {
                //stop chronometer
                mChronometer.stop();
                timerBtn.setText(R.string.chronometer_reset);
                timerBtn.setTag(2);
                addPenaltyTime.setTag(1);
                elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
                System.out.println("elapsed millis: " + elapsedMillis);
            } else if (status == 2) {
                //reset chronometer
                mChronometer.setBase(SystemClock.elapsedRealtime());
                timerBtn.setText(R.string.chronometer_start);
                timerBtn.setTag(0);
                addPenaltyTime.setTag(0);
            } else if (status == 3) {
                Intent intent = new Intent();
                intent.putExtra("com.comp.ninti.general.core.Event", event);
                Calendar calender = Calendar.getInstance();
                calender.setTimeInMillis(pointOfStop - mChronometer.getBase());
                System.out.println("time: " + (pointOfStop - mChronometer.getBase()));
                intent.putExtra("SCORE", TimeUtil.getPointsFromTime(rule.getBestTimePoints(), rule.getWorstTimePoints(), rule.getWorstTime(), rule.getBestTime(), calender.get(Calendar.SECOND)));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(Timer.this, "Unknown Button operation", Toast.LENGTH_SHORT).show();
            }
            //mChronometer.setFormat("Formatted time (%s)");
            //mChronometer.setFormat(null);
        } else if (v.getId() == R.id.addPenaltyTime) {
            final int status = (Integer) addPenaltyTime.getTag();
            if (status == 0) {
                Toast.makeText(Timer.this, "Take a Time first", Toast.LENGTH_SHORT).show();
                return;
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(Timer.this);
                //should be number
                edittext.setInputType(2);
                edittext.setText("0");
                edittext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edittext.setText("");
                    }
                });
                alert.setMessage("Specify Amount in Seconds");
                alert.setTitle("Add Penalty Time");

                alert.setView(edittext);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        //OR
                        String dialogText = edittext.getText().toString();
                        addedPenaltyTime = Long.valueOf(dialogText) * 1000;
                        pointOfStop = SystemClock.elapsedRealtime();
                        mChronometer.setBase(pointOfStop - (elapsedMillis + addedPenaltyTime));
                        timerBtn.setText(R.string.SAVE);
                        timerBtn.setTag(3);
                    }
                });

                alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                //tries are done one after another
                alert.show();
            }
        }
    }


    private void refresh() {
        addPenaltyTime.setTag(0);
        timerBtn.setTag(0);
        timerBtn.setText(R.string.chronometer_start);
        mChronometer.stop();
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }
}


