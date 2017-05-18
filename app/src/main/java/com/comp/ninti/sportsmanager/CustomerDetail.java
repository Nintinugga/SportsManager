package com.comp.ninti.sportsmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.general.Customer;

public class CustomerDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        Button addCustomerBtn = (Button) findViewById(R.id.addCustomer);
        addCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                if (name == null || name.isEmpty() || name.equals(R.string.Name)) {
                    Toast.makeText(CustomerDetail.this, "Use an actual Name!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Customer customer = new Customer(name);
                    DbHandler dbHandler = new DbHandler(CustomerDetail.this, "", null, 1);
                    long returnVal;
                    if ((returnVal = dbHandler.getWritableDatabase().insert(CustomerContract.CUSTOMER.TABLE_NAME, null, CustomerContract.getInsert(customer))) == -1) {
                        Toast.makeText(CustomerDetail.this, "Error while inserting!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CustomerDetail.this, "Added Customer: " + customer.getName() + " on position " + returnVal,
                                Toast.LENGTH_LONG).show();
                    }
                    dbHandler.close();

                }
            }
        });
    }
}
