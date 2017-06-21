package com.comp.ninti.sportsmanager;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.fragments.EventAttempts;
import com.comp.ninti.general.RuleType;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Event;

import java.util.ArrayList;

public class EventStarted extends AppCompatActivity implements EventAttempts.OnFragmentInteractionListener{
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    Event event;
    DbHandler dbHandler;
    ArrayList<Discipline> disciplines;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_started);
        event = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Event");
        for(long lon: event.getCustomers())
            System.out.println(lon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Started Event " + event.getName());
        getSupportActionBar().setSubtitle(event.getDate());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAllDisciplines();
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, disciplines.toArray(new Discipline[disciplines.size()]));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Discipline disc = (Discipline)spinner.getSelectedItem();
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.content_frame, EventAttempts.newInstance(disc.getId(), event.getId()));
                mFragmentTransaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(spinnerArrayAdapter);
        //Intent intent = new Intent();
        //intent.putExtra("com.comp.ninti.general.core.Event", event);
        //setResult(RESULT_OK, intent);
    }



    private void getAllDisciplines() {
        dbHandler = new DbHandler(EventStarted.this, "", null, 1);
        Cursor mCursor = dbHandler.getDisciplinesById(event.getDisciplines());
        disciplines = new ArrayList<>();
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            disciplines.add(DbHandler.populateDiscipline(mCursor));
        }
        dbHandler.close();
    }

    @Override
    public void onFragmentInteraction(long eventCustomerEntryId, RuleType ruleType) {
        if(ruleType == RuleType.Default){
            System.out.println(RuleType.Default);
        }else if(ruleType == RuleType.Time){
            System.out.println(RuleType.Time);
        }
    }
}
