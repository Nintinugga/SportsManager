package com.comp.ninti.sportsmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.database.EventContract;
import com.comp.ninti.database.EventCustomerContract;
import com.comp.ninti.database.RuleContract;
import com.comp.ninti.general.core.Rule;
import com.comp.ninti.general.RuleType;

import java.util.LinkedList;

public class RuleDetail extends AppCompatActivity {
    int requestCode;
    Rule rule;
    Button addRuleBtn;
    private EditText ruleBestTime;
    private EditText ruleName;
    private EditText ruleWorstTime;
    private EditText bestTimePoints;
    private EditText worstTimePoints;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            DbHandler dbHandler = new DbHandler(RuleDetail.this, "", null, 1);
            LinkedList<Long> discIds = dbHandler.getAllDisciplineIDsRules(rule.getId());
            for(Long discId:discIds){
                dbHandler.getWritableDatabase().execSQL(DisciplineContract.getDeleteForDisciplineRule(rule.getId(), discId));
                dbHandler.getWritableDatabase().execSQL(EventCustomerContract.getDeleteByDi(discId));
                dbHandler.getWritableDatabase().execSQL(EventContract.getDeleteByDiId(discId));
                System.out.println(EventContract.getDeleteByDiId(discId));
            }
            System.out.println(DisciplineContract.getDeleteByRuleId(rule.getId()));
            System.out.println(RuleContract.getDelete(rule.getId()));
            dbHandler.getWritableDatabase().execSQL(DisciplineContract.getDeleteByRuleId(rule.getId()));
            dbHandler.getWritableDatabase().execSQL(RuleContract.getDelete(rule.getId()));
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
        setContentView(R.layout.activity_rule_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ruleBestTime = (EditText) findViewById(R.id.ruleBestTime);
        ruleName = (EditText) findViewById(R.id.ruleName);
        ruleWorstTime = (EditText) findViewById(R.id.ruleWorstTime);
        bestTimePoints = (EditText) findViewById(R.id.bestTimePoints);
        worstTimePoints = (EditText) findViewById(R.id.worstTimePoints);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ruleName.requestFocus();

        addRuleBtn = (Button) findViewById(R.id.addRule);
        requestCode = getIntent().getIntExtra("REQUESTCODE", 1);
        if (requestCode == 2) {
            rule = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Rule");
            setValues();
        }
        addRuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRule();
            }

        });
    }

    private void setValues() {
        getSupportActionBar().setTitle(getString(R.string.EditRule));
        addRuleBtn.setText(getString(R.string.SAVE));
    }

    private void addRule() {
        String name = ruleName.getText().toString();
        Rule rule = null;
        //TODO check if rule is already in db
        if (name == null || name.isEmpty()) {
            Toast.makeText(RuleDetail.this, "You need to name a Rule",
                    Toast.LENGTH_LONG).show();
            return;
        }
        String bestTimeSt = ruleBestTime.getText().toString();
        String worstTimeSt = ruleWorstTime.getText().toString();
        String bestTimePointsSt = bestTimePoints.getText().toString();
        String worstTimePointsSt = worstTimePoints.getText().toString();

        if (bestTimeSt.isEmpty() && worstTimeSt.isEmpty() && bestTimePointsSt.isEmpty() && worstTimePointsSt.isEmpty()) {
            rule = new Rule(name, RuleType.Default);
        } else if (!bestTimeSt.isEmpty() && !worstTimeSt.isEmpty() && !bestTimePointsSt.isEmpty() && !worstTimePointsSt.isEmpty()) {
            double bestTime = Double.valueOf(bestTimeSt);
            double worstTime = Double.valueOf(worstTimeSt);
            int bestTimePoints = Integer.valueOf(bestTimePointsSt);
            int worstTimePoints = Integer.valueOf(worstTimePointsSt);
            rule = new Rule(name, RuleType.Time, bestTime, bestTimePoints, worstTime, worstTimePoints);
        } else if (!bestTimeSt.isEmpty() || !worstTimeSt.isEmpty() || !bestTimePointsSt.isEmpty() || !worstTimePointsSt.isEmpty()) {
            Toast.makeText(RuleDetail.this, "Don't give points or time for a Default Rule. \n" +
                            "If you want to create a Time Rule, fill all Fields!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (rule == null) {
            Toast.makeText(RuleDetail.this, "Something went wrong with the Creation of the Rule. " +
                            "\n Try again and check if either all Fields are filled(For a Time Rule) " +
                            "or just the Name Field(For a Default Rule)",
                    Toast.LENGTH_LONG).show();
            return;
        }
        DbHandler dbHandler = new DbHandler(RuleDetail.this, "", null, 1);
        long returnVal;
        if ((returnVal = dbHandler.getWritableDatabase().insert(RuleContract.RULE.TABLE_NAME, null, RuleContract.getInsert(rule))) == -1) {
            Toast.makeText(RuleDetail.this, "Error while inserting!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RuleDetail.this, "Added Rule: " + rule.getName() + " on position " + returnVal,
                    Toast.LENGTH_LONG).show();
        }
        dbHandler.close();
        finish();
    }

}
