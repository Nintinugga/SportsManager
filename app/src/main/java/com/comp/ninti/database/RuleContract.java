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

    public static final String CREATE_TABLE = "CREATE TABLE " + RULE.TABLE_NAME + "("
            + RULE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RULE.COLUMN_NAME + " TEXT, "
            + RULE.COLUMN_TYPE + " TEXT, "
            + RULE.COLUMN_BESTTIME + " INTEGER, "
            + RULE.COLUMN_BESTTIMEPOINTS + " DOUBLE, "
            + RULE.COLUMN_WORSTTIME + " INTEGER, "
            + RULE.COLUMN_WORSTTIMEPOINTS + " DOUBLE);";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + RULE.TABLE_NAME;

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
