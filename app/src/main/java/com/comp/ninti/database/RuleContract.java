package com.comp.ninti.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.comp.ninti.general.Rule;
import com.comp.ninti.general.RuleType;

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

    public static Rule createRule(Cursor cursor) {
        Rule ruleToReturn = null;
        try {
            int ruleTypeIndex = cursor.getColumnIndexOrThrow(RuleContract.RULE.COLUMN_TYPE);
            int idIndex = cursor.getColumnIndexOrThrow(RuleContract.RULE._ID);
            int nameIndex = cursor.getColumnIndexOrThrow(RuleContract.RULE.COLUMN_NAME);
            RuleType ruleType = Enum.valueOf(RuleType.class, cursor.getString(ruleTypeIndex));
            if (ruleType.equals(RuleType.Default)) {
                ruleToReturn = new Rule(cursor.getString(nameIndex), ruleType, cursor.getLong(idIndex));
            } else {
                int worstTimeIndex = cursor.getColumnIndexOrThrow(RuleContract.RULE.COLUMN_WORSTTIME);
                int bestTimeIndex = cursor.getColumnIndexOrThrow(RuleContract.RULE.COLUMN_BESTTIME);
                int bestTimePointsIndex = cursor.getColumnIndexOrThrow(RuleContract.RULE.COLUMN_BESTTIMEPOINTS);
                int worstTimePointIndex = cursor.getColumnIndexOrThrow(RuleContract.RULE.COLUMN_WORSTTIMEPOINTS);
                ruleToReturn = new Rule(cursor.getString(nameIndex), ruleType, cursor.getDouble(bestTimeIndex),
                        cursor.getInt(bestTimePointsIndex), cursor.getDouble(worstTimeIndex), cursor.getInt(worstTimePointIndex), cursor.getLong(idIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        return ruleToReturn;
    }
}
