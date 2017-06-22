package com.comp.ninti.database;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.comp.ninti.general.core.EventCustomerEntry;

public class EventCustomerContract {
    private EventCustomerContract() {
    }

    public static final class EVENTCUSTOMER implements BaseColumns {
        public static final String TABLE_NAME = "eventcustomer";
        public static final String COLUMN_EV_ID = "eventId";
        public static final String COLUMN_CU_ID = "custId";
        public static final String COLUMN_DI_ID = "discId";
        public static final String COLUMN_ATTEMPT = "attempt";
        public static final String COLUMN_POINTS = "points";
    }

    public static final String CREATE_TABLE = "CREATE TABLE " + EVENTCUSTOMER.TABLE_NAME + "("
            + EVENTCUSTOMER._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENTCUSTOMER.COLUMN_EV_ID + " LONG, "
            + EVENTCUSTOMER.COLUMN_CU_ID + " LONG, "
            + EVENTCUSTOMER.COLUMN_DI_ID + " LONG, "
            + EVENTCUSTOMER.COLUMN_ATTEMPT + " INT, "
            + EVENTCUSTOMER.COLUMN_POINTS + " INT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + EVENTCUSTOMER.TABLE_NAME;

    public static ContentValues getInsert(EventCustomerEntry eventCustomerEntry) {
        ContentValues values = new ContentValues();
        values.put(EVENTCUSTOMER.COLUMN_EV_ID, eventCustomerEntry.getEvId());
        values.put(EVENTCUSTOMER.COLUMN_CU_ID, eventCustomerEntry.getCustId());
        values.put(EVENTCUSTOMER.COLUMN_DI_ID, eventCustomerEntry.getDiscId());
        values.put(EVENTCUSTOMER.COLUMN_ATTEMPT, eventCustomerEntry.getAttempt());
        values.putNull(EVENTCUSTOMER.COLUMN_POINTS);
        return values;
    }
}
