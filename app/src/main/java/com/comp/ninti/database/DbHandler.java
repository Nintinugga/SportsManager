package com.comp.ninti.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.comp.ninti.general.RuleType;
import com.comp.ninti.general.core.Customer;
import com.comp.ninti.general.core.Discipline;
import com.comp.ninti.general.core.Event;
import com.comp.ninti.general.core.Rule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sportsmanager.db";
    private static final int DATABASE_VERSION = 24;


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
        System.out.println("creating disciplinerule table");
        db.execSQL(DisciplineContract.CREATE_TABLE_DISCIPLINE_RULE);
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
        db.execSQL(DisciplineContract.DROP_TABLE_DISCIPLINE_RULE);
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

    public Cursor getAllEvents() {
        return this.getReadableDatabase().rawQuery("select * from " + EventContract.EVENT.TABLE_NAME, null);
    }

    public Cursor getDisciplinesById(List<Long> ids) {
        final StringBuilder query = new StringBuilder("select * from " + DisciplineContract.DISCIPLINE.TABLE_NAME);
        final String discEq = DisciplineContract.DISCIPLINE._ID + " = ";
        final String separator = " or ";
        query.append(" where ");
        for (Long id : ids) {
            query.append(discEq);
            query.append(id);
            query.append(separator);
        }
        query.delete(query.length() - separator.length(), query.length());
        return this.getReadableDatabase().rawQuery(query.toString(), null);
    }

    public Cursor getCustomerById(long id) {
        final String query = "select * from " + CustomerContract.CUSTOMER.TABLE_NAME + " where " + CustomerContract.CUSTOMER._ID + " = " + id;
        return this.getReadableDatabase().rawQuery(query, null);
    }

    public void setScore(long eventCustomerEntryId, int score) {
        final String query = "UPDATE " + EventCustomerContract.EVENTCUSTOMER.TABLE_NAME +
                " SET "
                + EventCustomerContract.EVENTCUSTOMER.COLUMN_POINTS + " = " + score +
                " WHERE "
                + EventCustomerContract.EVENTCUSTOMER._ID + " = " + eventCustomerEntryId + ";";
        this.getWritableDatabase().execSQL(query);
    }

    public Cursor getCustomersById(List<Long> ids) {
        final StringBuilder query = new StringBuilder("select * from " + CustomerContract.CUSTOMER.TABLE_NAME);
        final String discEq = CustomerContract.CUSTOMER._ID + " = ";
        final String separator = " or ";
        query.append(" where ");
        for (Long id : ids) {
            query.append(discEq);
            query.append(id);
            query.append(separator);
        }
        query.delete(query.length() - separator.length(), query.length());
        return this.getReadableDatabase().rawQuery(query.toString(), null);
    }

    /**
     * The calculated score can be retrieved from column 2 as int or via column name 'score'
     *
     * @param eventId
     * @return
     */
    public Cursor getLeaderBoard(long eventId) {
        final String query = "select "
                + EventCustomerContract.EVENTCUSTOMER._ID + ", "
                + EventCustomerContract.EVENTCUSTOMER.COLUMN_CU_ID
                + ", SUM(" + EventCustomerContract.EVENTCUSTOMER.COLUMN_POINTS + ") AS score from "
                + EventCustomerContract.EVENTCUSTOMER.TABLE_NAME + " where "
                + EventCustomerContract.EVENTCUSTOMER.COLUMN_EV_ID + " = " + eventId
                + " GROUP BY " + EventCustomerContract.EVENTCUSTOMER.COLUMN_CU_ID
                + " ORDER BY score DESC;";
        return this.getReadableDatabase().rawQuery(query, null);
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

    public Cursor getEventCustomerEntries(long discId, long evId) {
        String select = "select * from " + EventCustomerContract.EVENTCUSTOMER.TABLE_NAME
                + " where " + EventCustomerContract.EVENTCUSTOMER.COLUMN_DI_ID + " = " + discId
                + " AND " + EventCustomerContract.EVENTCUSTOMER.COLUMN_EV_ID + " = " + evId
                + " AND " + EventCustomerContract.EVENTCUSTOMER.COLUMN_POINTS + " IS NULL;";
        return this.getReadableDatabase().rawQuery(select, null);
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

    public boolean isEventIn(long eventId) {
        String query = "SELECT EXISTS(SELECT 1 FROM " + EventCustomerContract.EVENTCUSTOMER.TABLE_NAME + " WHERE "
                + EventCustomerContract.EVENTCUSTOMER.COLUMN_EV_ID + " = " + eventId + " LIMIT 1);";
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
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

    public LinkedList<Long> getAllDisciplineIDsRules(long ruleId) {
        LinkedList<Long> discIds = new LinkedList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("Select "
                + DisciplineContract.DISCIPLINE_RULE.COLUMN_DISC_ID
                        + " from " + DisciplineContract.DISCIPLINE_RULE.TABLE_NAME
                        + " where " + DisciplineContract.DISCIPLINE_RULE.COLUMN_RULE_ID + " = " + ruleId + ";", null);
        while (cursor.moveToNext()) {
            discIds.add(cursor.getLong(cursor.getColumnIndex(DisciplineContract.DISCIPLINE_RULE.COLUMN_DISC_ID)));
        }
        return discIds;
    }

    public static Customer populateCustomer(Cursor c) {
        return new Customer(c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_NAME)),
                c.getInt(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_AGE)), c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_EMAIL)),
                c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_PHONE)), c.getLong(c.getColumnIndex(CustomerContract.CUSTOMER._ID)));
    }

    public static Discipline populateDiscipline(Cursor c) {
        return new Discipline(c.getString(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_NAME)),
                c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_RULE_ID)), c.getInt(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_ATTEMPTS)),
                c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE._ID)));
    }

    private void createDefaultValues(SQLiteDatabase db) {
        Customer customer = new Customer("Jonas Huber", 23, "Jonas.Huber@rocketmail.com", "01718650307");
        Customer customer1 = new Customer("Sepp Huber", 23, "Jonas.Huber@rocketmail.com", "01718650307");
        Customer customer2 = new Customer("Woife Huber", 23, "Jonas.Huber@rocketmail.com", "01718650307");
        db.insert(CustomerContract.CUSTOMER.TABLE_NAME, null, CustomerContract.getInsert(customer));
        db.insert(CustomerContract.CUSTOMER.TABLE_NAME, null, CustomerContract.getInsert(customer1));
        db.insert(CustomerContract.CUSTOMER.TABLE_NAME, null, CustomerContract.getInsert(customer2));
        Rule rule = new Rule("Default1", RuleType.Default, 1L);
        Rule rule1 = new Rule("Default2", RuleType.Default, 2L);
        Rule rule2 = new Rule("Default3", RuleType.Default, 3L);
        Rule rule3 = new Rule("Time1", RuleType.Time, 20D, 40, 40D, 20, 4L);
        db.insert(RuleContract.RULE.TABLE_NAME, null, RuleContract.getInsert(rule));
        db.insert(RuleContract.RULE.TABLE_NAME, null, RuleContract.getInsert(rule1));
        db.insert(RuleContract.RULE.TABLE_NAME, null, RuleContract.getInsert(rule2));
        db.insert(RuleContract.RULE.TABLE_NAME, null, RuleContract.getInsert(rule3));
        Discipline discipline = new Discipline("Weitschussexperte", rule, 2);
        Discipline discipline1 = new Discipline("Sprungspezialist", rule, 2);
        Discipline discipline2 = new Discipline("Praezessionsschuetze", rule, 2);
        Discipline discipline3 = new Discipline("Ballkuenstler", rule3, 2);
        long id = db.insert(DisciplineContract.DISCIPLINE.TABLE_NAME, null, DisciplineContract.getInsert(discipline));
        long id1 = db.insert(DisciplineContract.DISCIPLINE.TABLE_NAME, null, DisciplineContract.getInsert(discipline1));
        long id2 = db.insert(DisciplineContract.DISCIPLINE.TABLE_NAME, null, DisciplineContract.getInsert(discipline2));
        long id3 = db.insert(DisciplineContract.DISCIPLINE.TABLE_NAME, null, DisciplineContract.getInsert(discipline3));
        db.insert(DisciplineContract.DISCIPLINE_RULE.TABLE_NAME, null, DisciplineContract.getInsertDisciplineRule(id, 1L));
        db.insert(DisciplineContract.DISCIPLINE_RULE.TABLE_NAME, null, DisciplineContract.getInsertDisciplineRule(id1, 1L));
        db.insert(DisciplineContract.DISCIPLINE_RULE.TABLE_NAME, null, DisciplineContract.getInsertDisciplineRule(id2, 1L));
        db.insert(DisciplineContract.DISCIPLINE_RULE.TABLE_NAME, null, DisciplineContract.getInsertDisciplineRule(id3, 4L));
        LinkedList<Long> disciplines = new LinkedList<>();
        disciplines.add(1L);
        disciplines.add(2L);
        disciplines.add(3L);
        disciplines.add(4L);
        LinkedList<Long> customers = new LinkedList<>();
        customers.add(1L);
        customers.add(2L);
        customers.add(3L);
        Event event = new Event("Fussballcamp", disciplines, customers, "2017-08-21 18:30");
        db.insert(EventContract.EVENT.TABLE_NAME, null, EventContract.getInsert(event));
    }

}
