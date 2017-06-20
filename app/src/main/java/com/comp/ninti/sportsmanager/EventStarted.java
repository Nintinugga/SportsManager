package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.comp.ninti.general.core.Event;

public class EventStarted extends AppCompatActivity {
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_started);
        event = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Event");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(event.getName());
        getSupportActionBar().setSubtitle(event.getDate());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = new Intent();
        intent.putExtra("com.comp.ninti.general.core.Event", event);
        setResult(RESULT_OK, intent);
    }


}
