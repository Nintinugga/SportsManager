package com.comp.ninti.database;


import android.content.ContentValues;
import android.provider.BaseColumns;

import com.comp.ninti.general.Rule;

public final class RuleContract {
    private RuleContract() {
    }

    public static final class RULE implements BaseColumns {
        public static final String TABLE_NAME = "rule";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_BESTTIME = "besttime";
        public static final String COLUMN_WORSTTIME = "worsttime";
        public static final String COLUMN_BESTTIMEPOINTS = "besttimepoints";
        public static final String COLUMN_WORSTTIMEPOINTS = "worsttimepoints";
    }

    public static final class TIMERULE {
        public static final String TABLE_NAME = "timerule";
        public static final String COLUMN_RULEID = "ruleid";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_POINTS = "points";
    }

    public static final String CREATE_TABLE_TIME = "CREATE TABLE " + TIMERULE.TABLE_NAME + "("
            + TIMERULE.COLUMN_RULEID + " INTEGER, " + TIMERULE.COLUMN_TIME + " DOUBLE, "
            + TIMERULE.COLUMN_POINTS + " INTEGER, FOREIGN KEY("
            + TIMERULE.COLUMN_RULEID + ") REFERENCES "
            + RULE.TABLE_NAME + "(" + RULE._ID + "));";

    public static final String CREATE_TABLE_RULE = "CREATE TABLE " + RULE.TABLE_NAME + "("
            + RULE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RULE.COLUMN_NAME + " TEXT, "
            + RULE.COLUMN_TYPE + " TEXT, "
            + RULE.COLUMN_BESTTIME + " INTEGER, "
            + RULE.COLUMN_BESTTIMEPOINTS + " DOUBLE, "
            + RULE.COLUMN_WORSTTIME + " INTEGER, "
            + RULE.COLUMN_WORSTTIMEPOINTS + " DOUBLE);";

    public static final String DROP_TABLE_RULE = "DROP TABLE IF EXISTS " + RULE.TABLE_NAME;
    public static final String DROP_TABLE_TIME = "DROP TABLE IF EXISTS " + TIMERULE.TABLE_NAME;

    public static String getDelete(Rule rule) {
        return "DELETE FROM " + RULE.TABLE_NAME + " WHERE "
                + RULE.COLUMN_NAME + "=\"" + rule.getName() + "\" AND "
                + RULE.COLUMN_TYPE + "=" + rule.getRuleType() + "\" ;";
    }

    public static ContentValues getInsert(Rule rule) {
        ContentValues values = new ContentValues();
        values.put(RULE.COLUMN_NAME, rule.getName());
        values.put(RULE.COLUMN_TYPE, rule.getRuleType().toString());
        values.put(RULE.COLUMN_BESTTIME, rule.getBestTime());
        values.put(RULE.COLUMN_BESTTIMEPOINTS, rule.getBestTimePoints());
        values.put(RULE.COLUMN_WORSTTIME, rule.getWorstTime());
        values.put(RULE.COLUMN_WORSTTIMEPOINTS, rule.getWorstTimePoints());
        return values;
    }
}
