package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.fragments.EventAttempts;
import com.comp.ninti.general.RuleType;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Event;
import com.comp.ninti.general.core.Rule;
import com.comp.ninti.pointsDetermination.DefaultPoints;
import com.comp.ninti.pointsDetermination.Timer;

import junit.framework.Assert;

import java.util.ArrayList;

public class EventStarted extends AppCompatActivity implements EventAttempts.OnFragmentInteractionListener {
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private int score = 0;
    private long currentEventCustomerId;
    private Discipline disc;
    Event event;
    DbHandler dbHandler;
    ArrayList<Discipline> disciplines;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_started);
        if (getIntent().getExtras() != null)
            event = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Event");
        for (long lon : event.getCustomers())
            System.out.println(lon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(event.getName());
        getSupportActionBar().setSubtitle(event.getDate());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAllDisciplines();
        spinner = (Spinner) findViewById(R.id.spinner);
        createSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.showLeaderBoard) {
            showLeaderBoard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLeaderBoard() {
        Intent intent = new Intent(EventStarted.this, LeaderBoard.class);
        intent.putExtra("com.comp.ninti.general.core.Event", event);
        EventStarted.this.startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_leadboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void createSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, disciplines.toArray(new Discipline[disciplines.size()]));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                disc = (Discipline) spinner.getSelectedItem();
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
    }


    private void getAllDisciplines() {
        dbHandler = new DbHandler(EventStarted.this, "", null, 1);
        Cursor mCursor = dbHandler.getDisciplinesById(event.getDisciplines());
        disciplines = new ArrayList<>();
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            disciplines.add(DbHandler.populateDiscipline(mCursor));
        }
        mCursor.close();
        dbHandler.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("on activity result");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                event = data.getExtras().getParcelable("com.comp.ninti.general.core.Event");
                score = data.getIntExtra("SCORE", 0);
                System.out.println("reached score: " + score);
                dbHandler = new DbHandler(EventStarted.this, "", null, 1);
                dbHandler.setScore(currentEventCustomerId, score);
                dbHandler.close();
            }
            if (resultCode == RESULT_CANCELED) {
                event = data.getExtras().getParcelable("com.comp.ninti.general.core.Event");
            }
        }
    }

    @Override
    public void onFragmentInteraction(long eventCustomerEntryId, Rule rule, String customerName, String attempt) {
        Intent intent;
        currentEventCustomerId = eventCustomerEntryId;
        if (rule.getRuleType() == RuleType.Default) {
            System.out.println(RuleType.Default);
            intent = new Intent(EventStarted.this, DefaultPoints.class);
        } else {
            System.out.println(RuleType.Time);
            Assert.assertEquals(rule.getRuleType(), RuleType.Time);
            intent = new Intent(EventStarted.this, Timer.class);
        }
        intent.putExtra("com.comp.ninti.general.core.Event", event);
        intent.putExtra("com.comp.ninti.general.core.Rule", rule);
        intent.putExtra("ATTEMPT", attempt);
        intent.putExtra("CUSTOMER", customerName);
        intent.putExtra("DISCIPLINE", disc.getName());
        EventStarted.this.startActivityForResult(intent, 1);
    }
}
