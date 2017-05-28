package com.comp.ninti.sportsmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.RuleContract;
import com.comp.ninti.general.Rule;
import com.comp.ninti.general.RuleType;

public class RuleDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText editText1 = (EditText) findViewById(R.id.ruleName);
        editText1.requestFocus();

        Button addCustomerBtn = (Button) findViewById(R.id.addRule);
        addCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.ruleName)).getText().toString();
                double bestTime = Double.valueOf(((EditText) findViewById(R.id.ruleBestTime)).getText().toString());
                double worstTime = Double.valueOf(((EditText) findViewById(R.id.ruleWorstTime)).getText().toString());
                int bestTimePoints = Integer.valueOf(((EditText) findViewById(R.id.bestTimePoints)).getText().toString());
                int worstTimePoints = Integer.valueOf(((EditText) findViewById(R.id.worstTimePoints)).getText().toString());

                Rule rule = new Rule(name, RuleType.Time, bestTime, bestTimePoints, worstTime, worstTimePoints);
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

        });
    }
}
