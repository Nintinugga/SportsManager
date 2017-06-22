package com.comp.ninti.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.EventCustomerContract;
import com.comp.ninti.sportsmanager.R;

public class EventCustomerAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public EventCustomerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.event_customer_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvCuName = (TextView) view.findViewById(R.id.text1);
        TextView tvAttempt = (TextView) view.findViewById(R.id.text2);
        TextView tvScore = (TextView) view.findViewById(R.id.text3);
        long cuID = cursor.getLong(cursor.getColumnIndex(EventCustomerContract.EVENTCUSTOMER.COLUMN_CU_ID));
        int score = cursor.getInt(cursor.getColumnIndex(EventCustomerContract.EVENTCUSTOMER.COLUMN_POINTS));
        int attempt = cursor.getInt(cursor.getColumnIndex(EventCustomerContract.EVENTCUSTOMER.COLUMN_ATTEMPT));
        tvAttempt.setText(context.getString(R.string.AttemptColon, attempt));
        tvScore.setText(context.getString(R.string.ScoreColon, score));

        DbHandler dbHandler = new DbHandler(context, "", null, 1);
        Cursor custCurs = dbHandler.getCustomerById(cuID);
        custCurs.moveToFirst();
        tvCuName.setText(custCurs.getString(custCurs.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_NAME)));
        custCurs.close();
        dbHandler.close();
    }
}