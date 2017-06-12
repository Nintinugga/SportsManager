package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.RuleContract;
import com.comp.ninti.general.NavigationUtil;
import com.comp.ninti.general.core.Rule;
import com.comp.ninti.general.RuleType;

public class RulesActivity extends AppCompatActivity {
    private DbHandler dbHandler;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return NavigationUtil.switchNavigation(item, RulesActivity.this);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);

        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RulesActivity.this, RuleDetail.class);
                RulesActivity.this.startActivity(myIntent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.rulesListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Rule clickedRule = new Rule(c.getString(c.getColumnIndex(RuleContract.RULE.COLUMN_NAME)),
                        Enum.valueOf(RuleType.class, c.getString(c.getColumnIndex(RuleContract.RULE.COLUMN_TYPE))),
                        c.getDouble(c.getColumnIndex(RuleContract.RULE.COLUMN_BESTTIME)),
                        c.getInt(c.getColumnIndex(RuleContract.RULE.COLUMN_BESTTIMEPOINTS)),
                        c.getDouble(c.getColumnIndex(RuleContract.RULE.COLUMN_WORSTTIME)),
                        c.getInt(c.getColumnIndex(RuleContract.RULE.COLUMN_WORSTTIMEPOINTS)));
                Toast.makeText(RulesActivity.this, "Clicked Rule: " + clickedRule.getName() + " dbID: " + id,
                        Toast.LENGTH_LONG).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        displayItems();
    }

    private void displayItems() {
        dbHandler = new DbHandler(RulesActivity.this, "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                dbHandler.getAllRules(),
                new String[]{RuleContract.RULE.COLUMN_NAME},
                new int[]{android.R.id.text1});
        ListView listView = (ListView) findViewById(R.id.rulesListView);
        listView.setAdapter(adapter);
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
        navigation.getMenu().getItem(2).setChecked(true);
        displayItems();
    }
}
