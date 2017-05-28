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

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.general.Customer;

public class CustomersActivity extends AppCompatActivity {
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton addNewCustomer = (ImageButton) findViewById(R.id.addNewCustomer);
        addNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CustomersActivity.this, CustomerDetail.class);
                CustomersActivity.this.startActivity(myIntent);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        displayItems();
        ListView listView = (ListView) findViewById(R.id.customersListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Customer clickedCustomer = new Customer(c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_NAME)),
                        c.getInt(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_AGE)), c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_EMAIL)),
                        c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_PHONE)));
                Toast.makeText(CustomersActivity.this, "Clicked Customer: " + clickedCustomer.getName() + " dbID: " + id,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void displayItems() {
        dbHandler = new DbHandler(CustomersActivity.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getAllCustomers(),
                new String[]{CustomerContract.CUSTOMER.COLUMN_NAME, CustomerContract.CUSTOMER.COLUMN_EMAIL},
                new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.customersListView);
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

