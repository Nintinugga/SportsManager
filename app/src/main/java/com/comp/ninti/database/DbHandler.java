package com.comp.ninti.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.comp.ninti.general.core.Customer;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Event;
import com.comp.ninti.general.core.Rule;
import com.comp.ninti.general.RuleType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sportsmanager.db";
    private static final int DATABASE_VERSION = 11;


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
        db.execSQL(EventContract.CREATE_TABLE);
        System.out.println("creating events table");
        db.execSQL(DisciplineContract.CREATE_TABLE);
        System.out.println("creating eventcustomer table");
        db.execSQL(EventCustomerContract.CREATE_TABLE);
        createDefaultValues(db);
        System.out.println("creating default values");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CustomerContract.DROP_TABLE);
        db.execSQL(DisciplineContract.DROP_TABLE);
        db.execSQL(RuleContract.DROP_TABLE_RULE);
        db.execSQL(EventContract.DROP_TABLE);
        db.execSQL(EventCustomerContract.DROP_TABLE);
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

    public Cursor getAllEvents(){
        return this.getReadableDatabase().rawQuery("select * from " + EventContract.EVENT.TABLE_NAME, null);
    }

    public Cursor getDisciplinesById(List<Long> ids){
        final StringBuilder query = new StringBuilder("select * from " + DisciplineContract.DISCIPLINE.TABLE_NAME);
        final String discEq = DisciplineContract.DISCIPLINE._ID + " = ";
        final String separator = " or ";
                query.append(" where ");
        for(Long id: ids){
            query.append(discEq);
            query.append(id);
            query.append(separator);
        }
        query.delete(query.length()-separator.length(), query.length());
        return this.getReadableDatabase().rawQuery(query.toString(), null);
    }

    public Cursor getCustomersById(List<Long> ids){
        final StringBuilder query = new StringBuilder("select * from " + CustomerContract.CUSTOMER.TABLE_NAME);
        final String discEq = CustomerContract.CUSTOMER._ID + " = ";
        final String separator = " or ";
        query.append(" where ");
        for(Long id: ids){
            query.append(discEq);
            query.append(id);
            query.append(separator);
        }
        query.delete(query.length()-separator.length(), query.length());
        return this.getReadableDatabase().rawQuery(query.toString(), null);
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

    private void createDefaultValues(SQLiteDatabase db){
        Customer customer = new Customer("Jonas Huber", 23, "Jonas.Huber@rocketmail.com", "01718650307");
        Customer customer1 = new Customer("Sepp Huber", 23, "Jonas.Huber@rocketmail.com", "01718650307");
        Customer customer2 = new Customer("Woife Huber", 23, "Jonas.Huber@rocketmail.com", "01718650307");
        db.insert(CustomerContract.CUSTOMER.TABLE_NAME, null, CustomerContract.getInsert(customer));
        db.insert(CustomerContract.CUSTOMER.TABLE_NAME, null, CustomerContract.getInsert(customer1));
        db.insert(CustomerContract.CUSTOMER.TABLE_NAME, null, CustomerContract.getInsert(customer2));
        Rule rule = new Rule("Default1", RuleType.Default);
        Rule rule1 = new Rule("Default2", RuleType.Default);
        Rule rule2 = new Rule("Default3", RuleType.Default);
        db.insert(RuleContract.RULE.TABLE_NAME, null, RuleContract.getInsert(rule));
        db.insert(RuleContract.RULE.TABLE_NAME, null, RuleContract.getInsert(rule1));
        db.insert(RuleContract.RULE.TABLE_NAME, null, RuleContract.getInsert(rule2));
        Discipline discipline = new Discipline("Weitschussexperte", rule, 2);
        Discipline discipline1 = new Discipline("Sprintmeister", rule, 2);
        Discipline discipline2 = new Discipline("WasErAuchSonstMacht", rule, 2);
        db.insert(DisciplineContract.DISCIPLINE.TABLE_NAME, null, DisciplineContract.getInsert(discipline));
        db.insert(DisciplineContract.DISCIPLINE.TABLE_NAME, null, DisciplineContract.getInsert(discipline1));
        db.insert(DisciplineContract.DISCIPLINE.TABLE_NAME, null, DisciplineContract.getInsert(discipline2));
        LinkedList<Long> disciplines = new LinkedList<>();
        disciplines.add(1l);
        LinkedList<Long> customers = new LinkedList<>();
        customers.add(1l);
        Event event = new Event("Fussballcamp", disciplines, customers,"2017-08-21 18:30");
        db.insert(EventContract.EVENT.TABLE_NAME, null, EventContract.getInsert(event));
    }

}
