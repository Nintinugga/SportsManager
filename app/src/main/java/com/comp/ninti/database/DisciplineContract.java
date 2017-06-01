package com.comp.ninti.database;


import android.content.ContentValues;
import android.provider.BaseColumns;

import com.comp.ninti.general.Discipline;

public class DisciplineContract {
    private DisciplineContract() {
    }

    public static final class DISCIPLINE implements BaseColumns {
        public static final String TABLE_NAME = "discipline";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ATTEMPTS = "attempts";
        public static final String COLUMN_RULE = "rule";
    }

    public static final String CREATE_TABLE = "CREATE TABLE " + DISCIPLINE.TABLE_NAME + "("
            + DISCIPLINE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DISCIPLINE.COLUMN_NAME + " TEXT, " + DISCIPLINE.COLUMN_ATTEMPTS + " INTEGER, "
            + DISCIPLINE.COLUMN_RULE + " INTEGER, "
            + "FOREIGN KEY(" + DISCIPLINE.COLUMN_RULE + ") REFERENCES "+ RuleContract.RULE.TABLE_NAME+"(" + RuleContract.RULE._ID + "));";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DISCIPLINE.TABLE_NAME;

    public static String getDelete(Discipline discipline) {
        return "DELETE FROM " + DISCIPLINE.TABLE_NAME + " WHERE "
                + DISCIPLINE.COLUMN_NAME + "=\"" + discipline.getName() + "\" AND "
                + DISCIPLINE.COLUMN_ATTEMPTS + "=" + discipline.getAttempts() + " AND "
                + DISCIPLINE.COLUMN_RULE + "=" + discipline.getRule().getId() + " ;";
    }

    public static ContentValues getInsert(Discipline discipline) {
        ContentValues values = new ContentValues();
        values.put(DISCIPLINE.COLUMN_NAME, discipline.getName());
        values.put(DISCIPLINE.COLUMN_ATTEMPTS, discipline.getAttempts());
        values.put(DISCIPLINE.COLUMN_RULE, discipline.getRule().getId());
        return values;
    }
}
