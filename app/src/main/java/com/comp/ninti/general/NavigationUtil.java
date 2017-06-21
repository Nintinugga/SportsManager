package com.comp.ninti.general;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.comp.ninti.sportsmanager.CustomersActivity;
import com.comp.ninti.sportsmanager.DisciplinesActivity;
import com.comp.ninti.sportsmanager.EventsActivity;
import com.comp.ninti.sportsmanager.R;
import com.comp.ninti.sportsmanager.RulesActivity;
import com.comp.ninti.pointsDetermination.Timer;


public class NavigationUtil {
    public static boolean switchNavigation(@NonNull MenuItem item, @NonNull Context context) {
        Intent myIntent;
        switch (item.getItemId()) {
            case R.id.navigation_event:
                System.out.println("events Clicked");
                myIntent = new Intent(context, EventsActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                context.startActivity(myIntent);
                return true;
            case R.id.navigation_customers:
                System.out.println("Costumers Clicked");
                myIntent = new Intent(context, CustomersActivity.class);
                context.startActivity(myIntent);
                return true;
            case R.id.navigation_rules:
                System.out.println("rules Clicked");
                myIntent = new Intent(context, RulesActivity.class);
                context.startActivity(myIntent);
                return true;
            case R.id.navigation_disciplines:
                System.out.println("disciplines Clicked");
                myIntent = new Intent(context, DisciplinesActivity.class);
                context.startActivity(myIntent);
                return true;
        }
        return false;
    }
}

