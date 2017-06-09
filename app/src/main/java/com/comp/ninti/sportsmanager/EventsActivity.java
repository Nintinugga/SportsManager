package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DbListUtil;
import com.comp.ninti.database.EventContract;
import com.comp.ninti.general.core.Event;

public class EventsActivity extends AppCompatActivity {
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(EventsActivity.this, EventDetail.class);
                EventsActivity.this.startActivity(myIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.eventsLV);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Event clickedEvent = new Event(c.getLong(c.getColumnIndex(EventContract.EVENT._ID)),
                        c.getString(c.getColumnIndex(EventContract.EVENT.COLUMN_NAME)),
                        DbListUtil.convertStringToLongList(c.getString(c.getColumnIndex(EventContract.EVENT.COLUMN_DISCIPLINES))),
                        c.getString(c.getColumnIndex(EventContract.EVENT.COLUMN_DATE)));
                Intent myIntent = new Intent(EventsActivity.this, EventStart.class);
                myIntent.putExtra("com.comp.ninti.general.core.Event", clickedEvent);
                EventsActivity.this.startActivity(myIntent);
            }
        });

        displayItems();
    }

    private void displayItems() {
        dbHandler = new DbHandler(EventsActivity.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getAllEvents(),
                new String[]{EventContract.EVENT.COLUMN_NAME, EventContract.EVENT.COLUMN_DATE},
                new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.eventsLV);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null)
            dbHandler.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayItems();
    }


}
