package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.database.EventCustomerContract;
import com.comp.ninti.general.core.Customer;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Event;
import com.comp.ninti.general.core.EventCustomerEntry;

import java.util.LinkedList;


public class EventStart extends AppCompatActivity {
    private final String TAG = getClass().getName();
    private Event event;
    private DbHandler dbHandler;
    private ListView disciplinesListView, customersListView;
    private Button btnStartEvent;
    private SimpleCursorAdapter discAdapter, custAdapter;
    private LinkedList<Discipline> disciplines;

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
        btnStartEvent = (Button) findViewById(R.id.btnStartEvent);
        disciplinesListView = (ListView) findViewById(R.id.selectedDisciplinesLv);
        customersListView = (ListView) findViewById(R.id.selectedCustomersLv);
        setDisciplinesClickListener();
        setCustomerClickListener();
        displayDisciplinesItems();
        displayCustomersItems();
        btnStartEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler = new DbHandler(EventStart.this, "", null, 1);

                if (!dbHandler.isEventIn(event.getId())) {
                    Toast.makeText(EventStart.this, "Building event table for id: " + event.getId(),
                            Toast.LENGTH_LONG).show();
                    createEventCustomerTable();
                } else {
                    Toast.makeText(EventStart.this, "Event table was already created for this id: " + event.getId(),
                            Toast.LENGTH_LONG).show();
                }
                dbHandler.close();
                Intent myIntent = new Intent(EventStart.this, EventStarted.class);
                myIntent.putExtra("com.comp.ninti.general.core.Event", event);
                EventStart.this.startActivity(myIntent);
            }
        });
    }

    private void createEventCustomerTable() {
        dbHandler = new DbHandler(EventStart.this, "", null, 1);
        disciplines = new LinkedList<>();
        for (int i = 0; i < discAdapter.getCount(); i++) {
            Object obj = discAdapter.getItem(i);
            Discipline discipline = DbHandler.populateDiscipline((Cursor) obj);
            disciplines.add(discipline);
        }
        for (Discipline disc : disciplines) {
            for (Long custId : event.getCustomers()) {
                for (int attempt = 1; attempt <= disc.getAttempts(); attempt++) {
                    EventCustomerEntry eventCustomerEntry = new EventCustomerEntry(event.getId(), custId, disc.getId(), attempt);
                    long returnVal;
                    //fail
                    if ((returnVal = dbHandler.getWritableDatabase().insert(EventCustomerContract.EVENTCUSTOMER.TABLE_NAME, null, EventCustomerContract.getInsert(eventCustomerEntry))) == -1) {
                        Toast.makeText(EventStart.this, "Error while inserting: " + eventCustomerEntry.toString(),
                                Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Error while inserting: " + eventCustomerEntry.toString());
                        dbHandler.close();
                        return;
                        //success
                    } else {
                        Log.i(TAG, "Added eventcustomerentry: " + eventCustomerEntry.toString() + " on position " + returnVal);
                    }
                }
            }
        }
        dbHandler.close();
    }

    private void setCustomerClickListener() {
        customersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Customer clickedCustomer = new Customer(c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_NAME)),
                        c.getInt(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_AGE)), c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_EMAIL)),
                        c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_PHONE)), c.getLong(c.getColumnIndex(CustomerContract.CUSTOMER._ID)));
                Toast.makeText(EventStart.this, "Clicked Discipline: " + clickedCustomer.getName() + " dbID: " + id,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDisciplinesClickListener() {
        disciplinesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Discipline clickedDiscipline = new Discipline(c.getString(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_NAME)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_RULE_ID)), c.getInt(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_ATTEMPTS)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE._ID)));
                Toast.makeText(EventStart.this, "Clicked Discipline: " + clickedDiscipline.getName() + " dbID: " + id,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayCustomersItems() {
        dbHandler = new DbHandler(EventStart.this, "", null, 1);
        custAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getCustomersById(event.getCustomers()),
                new String[]{CustomerContract.CUSTOMER.COLUMN_NAME, CustomerContract.CUSTOMER.COLUMN_EMAIL},
                new int[]{android.R.id.text1, android.R.id.text2});
        customersListView.setAdapter(custAdapter);
        dbHandler.close();
    }

    private void displayDisciplinesItems() {
        dbHandler = new DbHandler(EventStart.this, "", null, 1);
        discAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getDisciplinesById(event.getDisciplines()),
                new String[]{DisciplineContract.DISCIPLINE.COLUMN_NAME, DisciplineContract.DISCIPLINE.COLUMN_RULE_ID},
                new int[]{android.R.id.text1, android.R.id.text2});
        disciplinesListView.setAdapter(discAdapter);
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
        displayCustomersItems();
        displayDisciplinesItems();
    }

}
