package com.comp.ninti.sportsmanager;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Event;

public class EventStart extends AppCompatActivity {
    private Event event;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Event");
        setContentView(R.layout.activity_event_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(event.getName());
        getSupportActionBar().setSubtitle(event.getDate());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listView = (ListView) findViewById(R.id.selectedDisciplinesLv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Discipline clickedDiscipline = new Discipline(c.getString(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_NAME)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_RULE)), c.getInt(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_ATTEMPTS)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE._ID)));
                Toast.makeText(EventStart.this, "Clicked Discipline: " + clickedDiscipline.getName() + " dbID: " + id,
                        Toast.LENGTH_LONG).show();
            }
        });
        displayItems();
    }
    private void displayItems() {
        dbHandler = new DbHandler(EventStart.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getDisciplinesById(event.getDisciplines()),
                new String[]{DisciplineContract.DISCIPLINE.COLUMN_NAME, DisciplineContract.DISCIPLINE.COLUMN_RULE},
                new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.selectedDisciplinesLv);
        listView.setAdapter(adapter);
    }
}
