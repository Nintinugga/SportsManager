package com.comp.ninti.database;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.comp.ninti.general.core.Customer;


public final class CustomerContract {
    private CustomerContract() {
    }

    public static final class CUSTOMER implements BaseColumns {
        public static final String TABLE_NAME = "customer";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE = "phone";
    }

    public static final String CREATE_TABLE = "CREATE TABLE " + CUSTOMER.TABLE_NAME + "("
            + CUSTOMER._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CUSTOMER.COLUMN_NAME + " TEXT, " + CUSTOMER.COLUMN_AGE + " INTEGER, "
            + CUSTOMER.COLUMN_EMAIL + " TEXT, " + CUSTOMER.COLUMN_PHONE + " TEXT);";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + CUSTOMER.TABLE_NAME;

    public static String getDelete(Customer customer) {
        return "DELETE FROM " + CUSTOMER.TABLE_NAME + " WHERE "
                + CUSTOMER.COLUMN_NAME + "=\"" + customer.getName() + "\" AND "
                + CUSTOMER.COLUMN_AGE + "=" + customer.getAge() + " AND "
                + CUSTOMER.COLUMN_EMAIL + "=\"" + customer.getEmail() + "\" AND"
                + CUSTOMER.COLUMN_PHONE + "=\"" + customer.getPhone() + "\";";
    }

    public static String getDelete(long custId) {
        return "DELETE FROM " + CUSTOMER.TABLE_NAME + " WHERE " + CUSTOMER._ID + " = " + custId + ";";
    }

    public static ContentValues getInsert(Customer customer) {
        ContentValues values = new ContentValues();
        values.put(CUSTOMER.COLUMN_NAME, customer.getName());
        values.put(CUSTOMER.COLUMN_AGE, customer.getAge());
        values.put(CUSTOMER.COLUMN_EMAIL, customer.getEmail());
        values.put(CUSTOMER.COLUMN_PHONE, customer.getPhone());
        return values;
    }
}
