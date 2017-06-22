package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.comp.ninti.adapter.LeaderBoardAdapter;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.general.core.Event;

public class LeaderBoard extends AppCompatActivity {
    private Event event;
    private DbHandler dbHandler;
    private LeaderBoardAdapter leaderBoardAdapter;
    private ListView listView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getIntent().getExtras().getParcelable("com.comp.ninti.general.core.Event");
        Intent intent = new Intent();
        intent.putExtra("com.comp.ninti.general.core.Event", event);
        setResult(RESULT_CANCELED, intent);
        setContentView(R.layout.activity_leader_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.lvLeaderBoard);
        listView.setTextFilterEnabled(true);
        displayItems();
    }

    private void displayItems() {
        dbHandler = new DbHandler(LeaderBoard.this, "", null, 1);
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                leaderBoardAdapter = new LeaderBoardAdapter(
                        LeaderBoard.this,
                        dbHandler.getLeaderBoard(event.getId()),
                        0);
                listView.setAdapter(leaderBoardAdapter);
            }
        });
        dbHandler.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayItems();
    }
}
