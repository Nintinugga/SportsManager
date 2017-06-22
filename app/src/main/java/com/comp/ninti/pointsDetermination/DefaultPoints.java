package com.comp.ninti.pointsDetermination;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.ninti.general.core.Customer;
import com.comp.ninti.general.core.Event;
import com.comp.ninti.general.core.Rule;
import com.comp.ninti.sportsmanager.R;

public class DefaultPoints extends AppCompatActivity {
    private Event event;
    private Rule rule;
    private Button btnSave;
    private EditText etScore;
    private String customerName,attempt;
    private TextView tvDiscTitle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        setContentView(R.layout.activity_default_points);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(customerName);
        getSupportActionBar().setSubtitle(attempt);
        btnSave = (Button) findViewById(R.id.btnSave);
        etScore = (EditText) findViewById(R.id.etScore);
        tvDiscTitle = (TextView) findViewById(R.id.discTitle);
        tvDiscTitle.setText(getIntent().getStringExtra("DISCIPLINE"));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String score = etScore.getText().toString();
                if (score == null || score.isEmpty()) {
                    Toast.makeText(DefaultPoints.this, "No Score Set!",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("com.comp.ninti.general.core.Event", event);
                    intent.putExtra("SCORE", Integer.valueOf(score));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }
}
