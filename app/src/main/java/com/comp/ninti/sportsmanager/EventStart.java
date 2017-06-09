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

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.general.core.Customer;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Event;

public class EventStart extends AppCompatActivity {
    private Event event;
    private DbHandler dbHandler;
    private ListView disciplinesListView, customersListView;

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
        disciplinesListView = (ListView) findViewById(R.id.selectedDisciplinesLv);
        customersListView = (ListView) findViewById(R.id.selectedCustomersLv);
        disciplinesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        displayDisciplinesItems();
        displayCustomersItems();
    }
    private void displayCustomersItems() {
        dbHandler = new DbHandler(EventStart.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getCustomersById(event.getCustomers()),
                new String[]{CustomerContract.CUSTOMER.COLUMN_NAME, CustomerContract.CUSTOMER.COLUMN_EMAIL},
                new int[]{android.R.id.text1, android.R.id.text2});
        customersListView.setAdapter(adapter);
    }

    private void displayDisciplinesItems() {
        dbHandler = new DbHandler(EventStart.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getDisciplinesById(event.getDisciplines()),
                new String[]{DisciplineContract.DISCIPLINE.COLUMN_NAME, DisciplineContract.DISCIPLINE.COLUMN_RULE},
                new int[]{android.R.id.text1, android.R.id.text2});
        disciplinesListView.setAdapter(adapter);
    }
}
