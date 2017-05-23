package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;

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
    }

    private void displayItems(){
        dbHandler = new DbHandler(CustomersActivity.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                dbHandler.getAllCustomers(),
                new String[] {CustomerContract.CUSTOMER.COLUMN_NAME, CustomerContract.CUSTOMER.COLUMN_EMAIL},
                new int[] { android.R.id.text1, android.R.id.text2 });
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHandler != null)
            dbHandler.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayItems();
    }
}

