package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.general.core.Discipline;

public class DisciplinesActivity extends AppCompatActivity {
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplines);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(DisciplinesActivity.this, DisciplineDetail.class);
                DisciplinesActivity.this.startActivity(myIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ListView listView = (ListView) findViewById(R.id.disciplinesListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Discipline clickedDiscipline = new Discipline(c.getString(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_NAME)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_RULE)), c.getInt(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_ATTEMPTS)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE._ID)));
                Toast.makeText(DisciplinesActivity.this, "Clicked Discipline: " + clickedDiscipline.getName() + " dbID: " + id,
                        Toast.LENGTH_LONG).show();
            }
        });

        displayItems();
    }

    private void displayItems() {
        dbHandler = new DbHandler(DisciplinesActivity.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getAllDisciplines(),
                new String[]{DisciplineContract.DISCIPLINE.COLUMN_NAME, DisciplineContract.DISCIPLINE.COLUMN_RULE},
                new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.disciplinesListView);
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
