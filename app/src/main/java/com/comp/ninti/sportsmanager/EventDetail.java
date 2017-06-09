package com.comp.ninti.sportsmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DbListUtil;
import com.comp.ninti.database.EventContract;
import com.comp.ninti.dialogs.SelectCustomer;
import com.comp.ninti.dialogs.SelectDisciplines;
import com.comp.ninti.general.core.Customer;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Event;
import com.comp.ninti.general.TimeUtil;

import java.util.Calendar;
import java.util.LinkedList;

public class EventDetail extends AppCompatActivity implements SelectDisciplines.OnCompleteListener, SelectCustomer.OnCompleteListener {

    Button addEventBtn, btnSelectDisc, btnSelectCust;
    ImageButton btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime, eventName;
    private LinkedList<Discipline> disciplines;
    private LinkedList<Customer> customers;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        calendar = Calendar.getInstance();
        disciplines = new LinkedList<>();
        customers = new LinkedList<>();
        btnDatePicker = (ImageButton) findViewById(R.id.btn_date);
        btnTimePicker = (ImageButton) findViewById(R.id.btn_time);
        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        btnSelectDisc = (Button) findViewById(R.id.btnSelectDisc);
        btnSelectCust = (Button) findViewById(R.id.btnSelectCus);
        eventName = (EditText) findViewById(R.id.eventName);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);

        btnSelectDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDisciplines selectDisciplines = new SelectDisciplines();
                selectDisciplines.show(getFragmentManager(), "selectDisciplines");
            }
        });

        btnSelectCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCustomer selectCustomer = new SelectCustomer();
                selectCustomer.show(getFragmentManager(), "selectCustomer");
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnClick(btnDatePicker);
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnClick(btnTimePicker);
            }
        });
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addEvent())
                    finish();
            }
        });

    }

    private boolean addEvent() {
        String name = eventName.getText().toString();
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

        if (checkParameter(name, date, time) == false)
            return false;
        if (disciplines == null || disciplines.isEmpty()) {
            makeToast("Your Event needs one Discipline at least");
            return false;
        }
        if(customers == null || customers.isEmpty()){
            makeToast("Your Event needs one Customer at least");
            return false;
        }
        String dateTime = date + " " + time;
        Event event = new Event(name, DbListUtil.convertDisciplinesToList(disciplines), DbListUtil.convertCustomerToList(customers), dateTime);

        DbHandler dbHandler = new DbHandler(EventDetail.this, "", null, 1);
        long returnVal;
        if ((returnVal = dbHandler.getWritableDatabase().insert(EventContract.EVENT.TABLE_NAME, null, EventContract.getInsert(event))) == -1) {
            Toast.makeText(EventDetail.this, "Error while inserting!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EventDetail.this, "Added Event: " + event.getName() + " on position " + returnVal,
                    Toast.LENGTH_LONG).show();
        }
        dbHandler.close();
        return true;
    }

    public void onBtnClick(ImageButton btn) {

        if (btn == btnDatePicker) {

            // Get Current Date
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            // Launch Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            txtDate.setText(TimeUtil.dateFormat.format(calendar.getTime()));
                            if (txtTime.getText().toString().isEmpty())
                                onBtnClick(btnTimePicker);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (btn == btnTimePicker) {

            // Get Current Time
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            calendar.set(Calendar.MINUTE, minute);
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            txtTime.setText(TimeUtil.timeFormat.format(calendar.getTime()));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private boolean checkParameter(String name, String date, String time) {
        if (name == null || name.isEmpty()) {
            makeToast("Your Event needs a Name!");
            return false;
        }
        if (date == null || date.isEmpty()) {
            makeToast("Your Event needs a Date!");
            return false;
        }
        if (time == null || time.isEmpty()) {
            makeToast("Your Event needs a Time!");
            return false;
        }
        if (disciplines == null || disciplines.isEmpty()) {
            makeToast("Your Event needs one Discipline at least");
            return false;
        }
        return true;
    }

    private void makeToast(String toShow) {
        Toast.makeText(EventDetail.this, toShow,
                Toast.LENGTH_LONG).show();
        return;
    }

    @Override
    public void onCompleteSelectDisc(LinkedList<Discipline> selectedDisciplines) {
        this.disciplines.clear();
        this.disciplines.addAll(selectedDisciplines);
    }

    @Override
    public void onCompleteSelectCust(LinkedList<Customer> selectedCustomers) {
        this.customers.clear();
        this.customers.addAll(selectedCustomers);
    }

    public LinkedList<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(LinkedList<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public LinkedList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(LinkedList<Customer> customers) {
        this.customers = customers;
    }
}
