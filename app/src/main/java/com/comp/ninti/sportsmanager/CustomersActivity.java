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
import android.widget.Toast;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.general.NavigationUtil;
import com.comp.ninti.general.core.Customer;

public class CustomersActivity extends AppCompatActivity {
    private DbHandler dbHandler;
    private BottomNavigationView navigation;
    private int menuNr = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return NavigationUtil.switchNavigation(item, CustomersActivity.this);
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (NavigationUtil.switchNavigation(item, CustomersActivity.this))
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
        setContentView(R.layout.activity_customers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(menuNr).setChecked(true);
        ImageButton addNewCustomer = (ImageButton) findViewById(R.id.addNewCustomer);
        addNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int requestCode = 1;
                Intent myIntent = new Intent(CustomersActivity.this, CustomerDetail.class);
                myIntent.putExtra("REQUESTCODE", requestCode);
                CustomersActivity.this.startActivityForResult(myIntent, requestCode);

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
                int requestCode = 2;
                Intent myIntent = new Intent(CustomersActivity.this, CustomerDetail.class);
                myIntent.putExtra("REQUESTCODE", requestCode);
                Customer clickedCustomer = new Customer(c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_NAME)),
                        c.getInt(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_AGE)), c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_EMAIL)),
                        c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_PHONE)), c.getLong(c.getColumnIndex(CustomerContract.CUSTOMER._ID)));
                Toast.makeText(CustomersActivity.this, "Clicked Customer: " + clickedCustomer.getName() + " dbID: " + id,
                        Toast.LENGTH_LONG).show();
                myIntent.putExtra("com.comp.ninti.general.core.Customer", clickedCustomer);
                CustomersActivity.this.startActivityForResult(myIntent, requestCode);
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
        navigation.getMenu().getItem(menuNr).setChecked(true);
        displayItems();
    }
}

