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

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.general.Discipline;
import com.comp.ninti.general.TimeUtil;

import java.util.Calendar;
import java.util.LinkedList;

public class EventDetail extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private LinkedList<Discipline> disciplines;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        disciplines = new LinkedList<>();
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);

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
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            // Launch Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, monthOfYear);
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            txtDate.setText(TimeUtil.dateFormat.format(c.getTime()));
                            onBtnClick(btnTimePicker);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (btn == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            c.set(Calendar.MINUTE, minute);
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            txtTime.setText(TimeUtil.timeFormat.format(c.getTime()));
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
}
