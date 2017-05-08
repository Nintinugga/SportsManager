package com.comp.ninti.sportsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.customers) {
            System.out.println("Costumers Clicked");
            Intent myIntent = new Intent(MainActivity.this, CustomersActivity.class);
            MainActivity.this.startActivity(myIntent);
        } else if (v.getId() == R.id.disciplines) {
            System.out.println("disciplines Clicked");
            Intent myIntent = new Intent(MainActivity.this, DisciplinesActivity.class);
            MainActivity.this.startActivity(myIntent);
        } else if (v.getId() == R.id.rules) {
            System.out.println("rules Clicked");
        } else if (v.getId() == R.id.events) {
            System.out.println("events Clicked");
            Intent myIntent = new Intent(MainActivity.this, EventsActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        }
    }
}
