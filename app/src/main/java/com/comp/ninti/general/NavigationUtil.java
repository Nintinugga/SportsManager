package com.comp.ninti.general;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.comp.ninti.sportsmanager.CustomersActivity;
import com.comp.ninti.sportsmanager.DisciplinesActivity;
import com.comp.ninti.sportsmanager.EventsActivity;
import com.comp.ninti.sportsmanager.R;
import com.comp.ninti.sportsmanager.RulesActivity;


public class NavigationUtil {
    public static boolean switchNavigation(@NonNull MenuItem item, @NonNull Context context) {
        Intent myIntent;
        switch (item.getItemId()) {
            case R.id.navigation_event:
                System.out.println("events Clicked");
                try {
                    myIntent = NavUtils.getParentActivityIntent(context, EventsActivity.class);
                    NavUtils.navigateUpTo((Activity) context, myIntent);
                    return true;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
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

