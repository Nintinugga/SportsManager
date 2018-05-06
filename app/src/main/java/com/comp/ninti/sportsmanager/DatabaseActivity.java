package com.comp.ninti.sportsmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.comp.ninti.general.NavigationUtil;

public class DatabaseActivity extends AppCompatActivity {
    private ImageButton btnExport, btnImport;

    private BottomNavigationView navigation;
    private int menuNr = 4;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return NavigationUtil.switchNavigation(item, DatabaseActivity.this);
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(NavigationUtil.switchNavigation(item, DatabaseActivity.this))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setup navigation
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(menuNr).setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().getItem(menuNr).setChecked(true);
    }

}
