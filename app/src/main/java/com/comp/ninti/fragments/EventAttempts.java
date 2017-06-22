package com.comp.ninti.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.comp.ninti.adapter.EventCustomerAdapter;
import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.database.EventCustomerContract;
import com.comp.ninti.general.core.Rule;
import com.comp.ninti.sportsmanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventAttempts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventAttempts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventAttempts extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "discId";
    private static final String ARG_PARAM2 = "evId";
    private DbHandler dbHandler;
    private long discId, evId;
    private ListView listView;
    private View view;
    EventCustomerAdapter eventCustomerAdapter;

    private OnFragmentInteractionListener mListener;

    public EventAttempts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventAttempts.
     */
    public static EventAttempts newInstance(long discId, long evId) {
        EventAttempts fragment = new EventAttempts();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, discId);
        args.putLong(ARG_PARAM2, evId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            discId = getArguments().getLong(ARG_PARAM1);
            evId = getArguments().getLong(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_attempts, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        displayItems(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getAdapter().getItem(position);
                c.moveToPosition(position);
                Cursor cursorDisc = dbHandler.getReadableDatabase().rawQuery("select " + DisciplineContract.DISCIPLINE.COLUMN_RULE_ID
                        + " from " + DisciplineContract.DISCIPLINE.TABLE_NAME
                        + " where " + DisciplineContract.DISCIPLINE._ID
                        + " = " + c.getLong(c.getColumnIndex(EventCustomerContract.EVENTCUSTOMER.COLUMN_DI_ID)), null);
                cursorDisc.moveToFirst();
                Rule rule = dbHandler.getSpecificRuleById(cursorDisc.getLong(cursorDisc.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_RULE_ID)));
                cursorDisc.close();
                TextView tvCuName = (TextView) view.findViewById(R.id.text1);
                TextView tvAttempt = (TextView) view.findViewById(R.id.text2);
                onFragmentInterAction(c.getLong(c.getColumnIndex(EventCustomerContract.EVENTCUSTOMER._ID)), rule, tvCuName.getText().toString(), tvAttempt.getText().toString());
            }
        });
        return view;
    }

    private void displayItems(final View view) {
        dbHandler = new DbHandler(view.getContext(), "", null, 1);
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                eventCustomerAdapter = new EventCustomerAdapter(
                        view.getContext(),
                        dbHandler.getEventCustomerEntries(discId, evId),
                        0);

                listView.setAdapter(eventCustomerAdapter);
            }
        });
        dbHandler.close();
    }

    public void onFragmentInterAction(long eventCustomerEntryId, Rule rule, String customerName, String attempt) {
        if (mListener != null) {
            mListener.onFragmentInteraction(eventCustomerEntryId, rule, customerName, attempt);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(long eventCustomerEntryId, Rule rule, String customerName, String attempt);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayItems(view);
    }
}
