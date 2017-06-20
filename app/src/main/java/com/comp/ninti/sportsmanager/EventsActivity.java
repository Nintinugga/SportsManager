package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DbListUtil;
import com.comp.ninti.database.EventContract;
import com.comp.ninti.general.NavigationUtil;
import com.comp.ninti.general.core.Event;

public class EventsActivity extends AppCompatActivity {
    private DbHandler dbHandler;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return NavigationUtil.switchNavigation(item, EventsActivity.this);
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(NavigationUtil.switchNavigation(item, EventsActivity.this))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);
        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(EventsActivity.this, EventDetail.class);
                EventsActivity.this.startActivity(myIntent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.eventsLV);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Event clickedEvent = new Event(c.getLong(c.getColumnIndex(EventContract.EVENT._ID)),
                        c.getString(c.getColumnIndex(EventContract.EVENT.COLUMN_NAME)),
                        DbListUtil.convertStringToLongList(c.getString(c.getColumnIndex(EventContract.EVENT.COLUMN_DISCIPLINES))),
                        DbListUtil.convertStringToLongList(c.getString(c.getColumnIndex(EventContract.EVENT.COLUMN_CUSTOMERS))),
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
        dbHandler.close();
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
        navigation.getMenu().getItem(0).setChecked(true);
        displayItems();
    }


}
