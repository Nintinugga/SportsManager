package com.comp.ninti.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.comp.ninti.general.Rule;
import com.comp.ninti.general.RuleType;

import org.json.JSONArray;
import org.json.JSONObject;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sportsmanager.db";
    private static final int DATABASE_VERSION = 5;


    public DbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("creating customers table");
        db.execSQL(CustomerContract.CREATE_TABLE);
        System.out.println("creating rules table");
        db.execSQL(RuleContract.CREATE_TABLE_RULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CustomerContract.DROP_TABLE);
        db.execSQL(RuleContract.DROP_TABLE_RULE);
        onCreate(db);
    }

    public Cursor getAllCustomers() {
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from " + CustomerContract.CUSTOMER.TABLE_NAME, null);
        return cursor;
    }

    public Cursor getAllRules() {
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from " + RuleContract.RULE.TABLE_NAME, null);
        return cursor;
    }

    /**
     * Returns the Rule with the given ruleName
     *
     * @param ruleName the name of the rule you want to get
     * @return the Rule with the given ruleName or null if none was found
     */
    public Rule getSpecificRule(String ruleName) {
        Rule ruleToReturn = null;
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from " + RuleContract.RULE.TABLE_NAME + " where " + RuleContract.RULE.COLUMN_NAME + " = \"" + ruleName + "\"", null);
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
        return ruleToReturn;
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
