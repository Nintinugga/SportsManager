package com.comp.ninti.database;


import android.content.ContentValues;
import android.provider.BaseColumns;

import com.comp.ninti.general.Rule;

public final class RuleContract {
    private RuleContract(){};
    public static final class RULE implements BaseColumns {
        public static final String TABLE_NAME = "rule";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_ATTEMPTS = "defaultAttempts";
    }

    public static final String CREATE_TABLE = "CREATE TABLE " + RULE.TABLE_NAME + "("
            + RULE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RULE.COLUMN_NAME + " TEXT, " + RULE.COLUMN_TYPE + " TEXT, "
            + RULE.COLUMN_ATTEMPTS + " INTEGER);";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + RULE.TABLE_NAME;

    public static String getDelete(Rule rule) {
        return "DELETE FROM " + RULE.TABLE_NAME + " WHERE "
                + RULE.COLUMN_NAME + "=\"" + rule.getName() + "\" AND "
                + RULE.COLUMN_TYPE + "=" + rule.getRuleType() + " AND "
                + RULE.COLUMN_ATTEMPTS + "=\"" + rule.getDefaultAttempts() + "\" ;";
    }

    public static ContentValues getInsert(Rule rule) {
        ContentValues values = new ContentValues();
        values.put(RULE.COLUMN_NAME, rule.getName());
        values.put(RULE.COLUMN_TYPE, rule.getRuleType().toString());
        values.put(RULE.COLUMN_ATTEMPTS, rule.getDefaultAttempts());
        return values;
    }
}
