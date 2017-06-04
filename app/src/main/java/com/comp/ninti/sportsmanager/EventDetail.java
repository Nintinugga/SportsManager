package com.comp.ninti.sportsmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DbListUtil;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.database.EventContract;
import com.comp.ninti.general.Discipline;
import com.comp.ninti.general.Event;
import com.comp.ninti.general.TimeUtil;

import java.util.Calendar;
import java.util.LinkedList;

public class EventDetail extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker, addEventBtn;
    EditText txtDate, txtTime, eventName;
    private LinkedList<Discipline> disciplines;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar calendar;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        calendar = Calendar.getInstance();
        disciplines = new LinkedList<>();
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        eventName = (EditText) findViewById(R.id.eventName);

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
                addEvent();
                finish();
            }
        });

        ListView listView = (ListView) findViewById(R.id.eventDisciplinesLV);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Discipline clickedDiscipline = new Discipline(c.getString(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_NAME)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_RULE)), c.getInt(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_ATTEMPTS)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE._ID)));
                if (disciplines.contains(clickedDiscipline)) {
                    disciplines.remove(clickedDiscipline);
                } else {
                    disciplines.add(clickedDiscipline);
                }
            }
        });

        displayItems();
    }

    private void addEvent() {
        String name = eventName.getText().toString();

        String date = txtDate.getText().toString();

        String time = txtTime.getText().toString();
        if (checkParameter(name, date, time) == false)
            return;
        if (disciplines == null || disciplines.isEmpty()) {
            makeToast("Your Event needs one Discipline at least");
            return;
        }
        String dateTime = date + " " + time;
        Event event = new Event(name, DbListUtil.convertDisciplinesToList(disciplines), dateTime);

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
    }


    private void displayItems() {
        dbHandler = new DbHandler(EventDetail.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_activated_2,
                dbHandler.getAllDisciplines(),
                new String[]{DisciplineContract.DISCIPLINE.COLUMN_NAME, DisciplineContract.DISCIPLINE.COLUMN_RULE},
                new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.eventDisciplinesLV);
        listView.setAdapter(adapter);
    }


    public void onBtnClick(Button btn) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null)
            dbHandler.close();
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
}
