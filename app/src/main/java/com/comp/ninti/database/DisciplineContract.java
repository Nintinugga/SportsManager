package com.comp.ninti.database;


import android.content.ContentValues;
import android.provider.BaseColumns;

import com.comp.ninti.general.core.Discipline;

public class DisciplineContract {
    private DisciplineContract() {
    }

    public static final class DISCIPLINE implements BaseColumns {
        public static final String TABLE_NAME = "discipline";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ATTEMPTS = "attempts";
        public static final String COLUMN_RULE_ID = "rule";
    }

    public static final class DISCIPLINE_RULE implements BaseColumns {
        public static final String TABLE_NAME = "disciplinerule";
        public static final String COLUMN_DISC_ID = "discId";
        public static final String COLUMN_RULE_ID = "ruleID";
    }

    public static final String CREATE_TABLE_DISCIPLINE_RULE = "CREATE TABLE " + DISCIPLINE_RULE.TABLE_NAME + "("
            + DISCIPLINE_RULE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DISCIPLINE_RULE.COLUMN_RULE_ID + " LONG, "
            + DISCIPLINE_RULE.COLUMN_DISC_ID + " LONG);";

    public static final String DROP_TABLE_DISCIPLINE_RULE = "DROP TABLE IF EXISTS " + DISCIPLINE_RULE.TABLE_NAME;

    public static String getDeleteForDisciplineRule(long ruleId, long discId) {
        return "DELETE FROM " + DISCIPLINE_RULE.TABLE_NAME + " WHERE " + DISCIPLINE_RULE.COLUMN_RULE_ID + " = " + ruleId + " OR " + DISCIPLINE_RULE.COLUMN_DISC_ID + " = " + discId;
    }

    public static ContentValues getInsertDisciplineRule(long discId, long ruleId) {
        ContentValues values = new ContentValues();
        values.put(DISCIPLINE_RULE.COLUMN_RULE_ID, ruleId);
        values.put(DISCIPLINE_RULE.COLUMN_DISC_ID, discId);
        return values;
    }

    public static final String CREATE_TABLE = "CREATE TABLE " + DISCIPLINE.TABLE_NAME + "("
            + DISCIPLINE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DISCIPLINE.COLUMN_NAME + " TEXT, "
            + DISCIPLINE.COLUMN_ATTEMPTS + " INTEGER, "
            + DISCIPLINE.COLUMN_RULE_ID + " LONG, "
            + "FOREIGN KEY(" + DISCIPLINE.COLUMN_RULE_ID + ") REFERENCES " + RuleContract.RULE.TABLE_NAME + "(" + RuleContract.RULE._ID + "));";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DISCIPLINE.TABLE_NAME;

    public static String getDelete(long diId) {
        return "DELETE FROM " + DISCIPLINE.TABLE_NAME + " WHERE " + DISCIPLINE._ID + " = " + diId + ";";
    }

    public static String getDeleteByRuleId(long ruleId) {
        return "DELETE FROM " + DISCIPLINE.TABLE_NAME + " WHERE " + DISCIPLINE.COLUMN_RULE_ID + " = " + ruleId + ";";
    }

    public static String getDelete(Discipline discipline) {
        return "DELETE FROM " + DISCIPLINE.TABLE_NAME + " WHERE "
                + DISCIPLINE.COLUMN_NAME + "=\"" + discipline.getName() + "\" AND "
                + DISCIPLINE.COLUMN_ATTEMPTS + "=" + discipline.getAttempts() + " AND "
                + DISCIPLINE.COLUMN_RULE_ID + "=" + discipline.getRule().getId() + " ;";
    }

    public static ContentValues getInsert(Discipline discipline) {
        ContentValues values = new ContentValues();
        values.put(DISCIPLINE.COLUMN_NAME, discipline.getName());
        values.put(DISCIPLINE.COLUMN_ATTEMPTS, discipline.getAttempts());
        values.put(DISCIPLINE.COLUMN_RULE_ID, discipline.getRule().getId());
        return values;
    }
}
