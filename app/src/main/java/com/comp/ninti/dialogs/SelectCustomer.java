package com.comp.ninti.dialogs;


import android.app.Activity;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.comp.ninti.database.CustomerContract;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.general.core.Customer;
import com.comp.ninti.sportsmanager.EventDetail;
import com.comp.ninti.sportsmanager.R;

import java.util.LinkedList;

public class SelectCustomer extends DialogFragment implements View.OnClickListener {
    private DbHandler dbHandler;
    private LinkedList<Customer> customers;
    private Button btnAdd, btnBack;
    private OnCompleteListener mListener;
    private ListView listView;

    public interface OnCompleteListener {
        void onCompleteSelectCust(LinkedList<Customer> customers);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customers, null);
        customers = new LinkedList<>();
        customers.addAll(((EventDetail)getActivity()).getCustomers());
        listView = (ListView) view.findViewById(R.id.customerListView);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        setCancelable(false);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Customer clickedCustomer = new Customer(c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_NAME)),
                        c.getInt(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_AGE)), c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_EMAIL)),
                        c.getString(c.getColumnIndex(CustomerContract.CUSTOMER.COLUMN_PHONE)), c.getLong(c.getColumnIndex(CustomerContract.CUSTOMER._ID)));
                if (customers.contains(clickedCustomer)) {
                    customers.remove(clickedCustomer);
                } else {
                    customers.add(clickedCustomer);
                }
            }
        });
        displayItems(view);
        for(Customer customer: customers){
            listView.setItemChecked((int)customer.getId()-1, true);
        }
        return view;
    }

    private void displayItems(View view) {
        dbHandler = new DbHandler(view.getContext(), "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(view.getContext(),
                android.R.layout.simple_list_item_activated_2,
                dbHandler.getAllCustomers(),
                new String[]{CustomerContract.CUSTOMER.COLUMN_NAME, CustomerContract.CUSTOMER.COLUMN_EMAIL},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
        dbHandler.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("destroying view");
        if(dbHandler != null)
            dbHandler.close();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            System.out.println("add was clicked");
            this.mListener.onCompleteSelectCust(customers);
            dismiss();
        } else if (v.getId() == R.id.btnBack) {
            System.out.println("back was clicked");
            dismiss();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            System.out.println("activity was attached");
            this.mListener = (SelectCustomer.OnCompleteListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
}
