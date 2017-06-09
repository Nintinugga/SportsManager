package com.comp.ninti.database;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.comp.ninti.general.core.Event;

public class EventContract {
    private EventContract() {
    }

    public static final class EVENT implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DISCIPLINES = "disciplines";
        public static final String COLUMN_DATE = "date";
    }

    public static final String CREATE_TABLE = "CREATE TABLE " + EVENT.TABLE_NAME + "("
            + EVENT._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT.COLUMN_NAME + " TEXT, " + EVENT.COLUMN_DATE + " TEXT, "
            + EVENT.COLUMN_DISCIPLINES + ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + EVENT.TABLE_NAME;

    public static ContentValues getInsert(Event event) {
        ContentValues values = new ContentValues();
        values.put(EVENT.COLUMN_NAME, event.getName());
        values.put(EVENT.COLUMN_DISCIPLINES, DbListUtil.convertListToString(event.getDisciplines()));
        values.put(EVENT.COLUMN_DATE, event.getDate());
        return values;
    }
}
