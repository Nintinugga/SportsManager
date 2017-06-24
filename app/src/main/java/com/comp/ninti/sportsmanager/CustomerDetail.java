package com.comp.ninti.sportsmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.EventContract;
import com.comp.ninti.database.EventCustomerContract;
import com.comp.ninti.general.NavigationUtil;
import com.comp.ninti.general.core.Customer;

import java.util.ArrayList;

public class CustomerDetail extends AppCompatActivity {
    private int requestCode;
    private Customer customer;
    private EditText etName, etEmail, etPhone;
    private Spinner spinner;
    Button addCustomerBtn;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            DbHandler dbHandler = new DbHandler(CustomerDetail.this, "", null, 1);
            dbHandler.getWritableDatabase().execSQL(CustomerContract.getDelete(customer.getId()));
            dbHandler.getWritableDatabase().execSQL(EventCustomerContract.getDeleteByCu(customer.getId()));
            dbHandler.getWritableDatabase().execSQL(EventContract.getDeleteByCuId(customer.getId()));
            System.out.println(EventContract.getDeleteByCuId(customer.getId()));
            dbHandler.close();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (requestCode == 2)
            getMenuInflater().inflate(R.menu.delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        etName = (EditText) findViewById(R.id.customerName);
        etEmail = (EditText) findViewById(R.id.customerEmail);
        etPhone = (EditText) findViewById(R.id.customerTelephone);
        spinner = (Spinner) findViewById(R.id.customerAge);
        addCustomerBtn = (Button) findViewById(R.id.addCustomer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createSpinnerForAge();

        requestCode = getIntent().getIntExtra("REQUESTCODE", 1);
        if (requestCode == 2) {
            System.out.println("hello requerst code 2");
            customer = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Customer");
            setValues();
        }

        addCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                int age = Integer.valueOf(((Spinner) findViewById(R.id.customerAge)).getSelectedItem().toString());
                if (name == null || name.isEmpty() || name.equals(R.string.Name)) {
                    Toast.makeText(CustomerDetail.this, "Use an actual Name!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Customer customer = new Customer(name, age, email, phone);
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
                    finish();
                }
            }
        });
        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard();
                    v.clearFocus();
                    Spinner spinner = (Spinner) findViewById(R.id.customerAge);
                    spinner.requestFocus();
                    spinner.performClick();
                    handled = true;
                }
                return handled;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editText1 = (EditText) findViewById(R.id.customerName);
        editText1.requestFocus();
    }

    private void createSpinnerForAge() {
        ArrayList<Integer> age = new ArrayList<Integer>();
        for (int i = 1; i <= 100; i++) {
            age.add(i);
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.customerAge);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setValues(){
        getSupportActionBar().setTitle(getString(R.string.EditCustomer));
        addCustomerBtn.setText(getString(R.string.SAVE));
        etName.setText(customer.getName());
        etPhone.setText(customer.getPhone());
        etEmail.setText(customer.getEmail());
        spinner.setSelection(customer.getAge()-1);
    }

}
