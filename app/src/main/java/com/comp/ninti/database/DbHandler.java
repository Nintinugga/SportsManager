package com.comp.ninti.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.comp.ninti.general.Rule;

import org.json.JSONArray;
import org.json.JSONObject;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sportsmanager.db";
    private static final int DATABASE_VERSION = 6;


    public DbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("creating customers table");
        db.execSQL(CustomerContract.CREATE_TABLE);
        System.out.println("creating rules table");
        db.execSQL(RuleContract.CREATE_TABLE_RULE);
        System.out.println("creating disciplines table");
        db.execSQL(DisciplineContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CustomerContract.DROP_TABLE);
        db.execSQL(DisciplineContract.DROP_TABLE);
        db.execSQL(RuleContract.DROP_TABLE_RULE);
        onCreate(db);
    }

    public Cursor getAllCustomers() {
        return this.getReadableDatabase().rawQuery("select * from " + CustomerContract.CUSTOMER.TABLE_NAME, null);
    }

    public Cursor getAllRules() {
        return this.getReadableDatabase().rawQuery("select * from " + RuleContract.RULE.TABLE_NAME, null);
    }

    public Cursor getAllDisciplines() {
        return this.getReadableDatabase().rawQuery("select * from " + DisciplineContract.DISCIPLINE.TABLE_NAME, null);
    }

    /**
     * Returns the Rule with the given ruleName
     *
     * @param ruleName the name of the rule you want to get
     * @return the Rule with the given ruleName or null if none was found
     */
    public Rule getSpecificRule(String ruleName) {
        String select = "select * from " + RuleContract.RULE.TABLE_NAME + " where " + RuleContract.RULE.COLUMN_NAME + " = \"" + ruleName + "\"";
        Cursor cursor = this.getReadableDatabase().rawQuery(select, null);
        cursor.moveToFirst();
        return RuleContract.createRule(cursor);
    }

    /**
     * Returns the Rule with the given id
     *
     * @param ruleId the id of the rule you want to get
     * @return the Rule with the given ruleId or null if none was found
     */
    public Rule getSpecificRuleById(long ruleId) {
        String select = "select * from " + RuleContract.RULE.TABLE_NAME + " where " + RuleContract.RULE._ID + " = " + ruleId + "";
        Cursor cursor = this.getReadableDatabase().rawQuery(select, null);
        cursor.moveToFirst();
        return RuleContract.createRule(cursor);
    }

    private JSONArray getResults(SQLiteDatabase myDataBase, String searchQuery) {

        Cursor cursor = myDataBase.rawQuery(searchQuery, null);

        JSONArray resultSet = new JSONArray();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {

                    try {

                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }

            }

            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        Log.d("TAG_NAME", resultSet.toString());
        return resultSet;

    }

}
