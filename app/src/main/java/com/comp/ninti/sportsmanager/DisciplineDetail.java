package com.comp.ninti.sportsmanager;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.database.EventContract;
import com.comp.ninti.database.EventCustomerContract;
import com.comp.ninti.database.RuleContract;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Rule;

import java.util.ArrayList;

public class DisciplineDetail extends AppCompatActivity {
    int requestCode;
    Discipline discipline;
    Spinner spinnerForAttempts, spinnerForRules;
    EditText etName;
    Button addDisciplineBtn;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            DbHandler dbHandler = new DbHandler(DisciplineDetail.this, "", null, 1);
            dbHandler.getWritableDatabase().execSQL(DisciplineContract.getDelete(discipline.getId()));
            dbHandler.getWritableDatabase().execSQL(EventCustomerContract.getDeleteByDi(discipline.getId()));
            dbHandler.getWritableDatabase().execSQL(EventContract.getDeleteByDiId(discipline.getId()));
            System.out.println(EventContract.getDeleteByDiId(discipline.getId()));
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
        setContentView(R.layout.activity_discipline_detail);
        etName = (EditText) findViewById(R.id.disciplineName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinnerForRules = (Spinner) findViewById(R.id.disciplineRuleSp);
        spinnerForAttempts = (Spinner) findViewById(R.id.disciplineAttemptsSp);
        createSpinnerForAttempts();
        createSpinnerForRules();
        addDisciplineBtn = (Button) findViewById(R.id.addDiscipline);
        requestCode = getIntent().getIntExtra("REQUESTCODE", 1);
        if (requestCode == 2) {
            discipline = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Discipline");
            setValues();
        }
        addDisciplineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDiscipline();
            }
        });
    }

    private void addDiscipline() {
        String name = etName.getText().toString();
        if (name == null || name.isEmpty()) {
            Toast.makeText(DisciplineDetail.this, "Your Discipline needs a Name!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Cursor spinnerOb = (Cursor) ((Spinner) findViewById(R.id.disciplineRuleSp)).getSelectedItem();
        int attempts = Integer.valueOf(((Spinner) findViewById(R.id.disciplineAttemptsSp)).getSelectedItem().toString());
        DbHandler dbHandler = new DbHandler(DisciplineDetail.this, "", null, 1);
        Rule rule = dbHandler.getSpecificRule(spinnerOb.getString(spinnerOb.getColumnIndex(RuleContract.RULE.COLUMN_NAME)));
        if (rule == null) {
            Toast.makeText(DisciplineDetail.this, "No rule found for" + spinnerOb.getString(spinnerOb.getColumnIndex(RuleContract.RULE.COLUMN_NAME)),
                    Toast.LENGTH_LONG).show();
            return;
        }
        Discipline discipline = new Discipline(name, rule, attempts);
        long returnVal;
        if ((returnVal = dbHandler.getWritableDatabase().insert(DisciplineContract.DISCIPLINE.TABLE_NAME, null, DisciplineContract.getInsert(discipline))) == -1) {
            Toast.makeText(DisciplineDetail.this, "Error while inserting!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(DisciplineDetail.this, "Added Discipline: " + discipline.getName() + " on position " + returnVal,
                    Toast.LENGTH_LONG).show();
            dbHandler.getWritableDatabase().insert(DisciplineContract.DISCIPLINE_RULE.TABLE_NAME, null, DisciplineContract.getInsertDisciplineRule(returnVal, rule.getId()));
        }
        dbHandler.close();
        finish();
    }

    private void createSpinnerForAttempts() {
        ArrayList<Integer> attempts = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            attempts.add(i);
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, attempts);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerForAttempts.setAdapter(spinnerArrayAdapter);
    }

    private void createSpinnerForRules() {
        DbHandler dbHandler = new DbHandler(DisciplineDetail.this, "", null, 1);
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, dbHandler.getAllRules(),
                new String[]{RuleContract.RULE.COLUMN_NAME},
                new int[]{android.R.id.text1});
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForRules.setAdapter(simpleCursorAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createSpinnerForRules();
        createSpinnerForAttempts();
    }

    private void setValues() {
        getSupportActionBar().setTitle(getString(R.string.EditDiscipline));
        addDisciplineBtn.setText(getString(R.string.SAVE));
        etName.setText(discipline.getName());
        spinnerForRules.setSelection((int) discipline.getRuleId() - 1);
        spinnerForAttempts.setSelection(discipline.getAttempts() - 1);
    }
}
