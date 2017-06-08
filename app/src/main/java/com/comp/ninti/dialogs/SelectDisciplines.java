package com.comp.ninti.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
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

import com.comp.ninti.database.DbHandler;
import com.comp.ninti.database.DisciplineContract;
import com.comp.ninti.general.Discipline;
import com.comp.ninti.sportsmanager.EventDetail;
import com.comp.ninti.sportsmanager.R;

import java.util.LinkedList;


public class SelectDisciplines extends DialogFragment implements View.OnClickListener {

    private DbHandler dbHandler;
    private LinkedList<Discipline> disciplines;
    private Button btnAdd, btnBack;
    private ListView listView;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            System.out.println("add was clicked");
            this.mListener.onComplete(disciplines);
            dismiss();
        } else if (v.getId() == R.id.btnBack) {
            System.out.println("back was clicked");
            dismiss();
        }
    }

    public interface OnCompleteListener {
        void onComplete(LinkedList<Discipline> selectedDisciplines);
    }

    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            System.out.println("activity was attached");
            this.mListener = (OnCompleteListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disciplines, null);
        System.out.println("INFLATING");
        disciplines = new LinkedList<>();
        disciplines.addAll(((EventDetail)getActivity()).getDisciplines());
        ListView listView = (ListView) view.findViewById(R.id.disciplinesListView);
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
                Discipline clickedDiscipline = new Discipline(c.getString(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_NAME)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_RULE)), c.getInt(c.getColumnIndex(DisciplineContract.DISCIPLINE.COLUMN_ATTEMPTS)),
                        c.getLong(c.getColumnIndex(DisciplineContract.DISCIPLINE._ID)));
                if (disciplines.contains(clickedDiscipline)) {
                    disciplines.remove(clickedDiscipline);
                } else {
                    disciplines.add(clickedDiscipline);
                }
            }
        });
        displayDisciplinesItems(view);
        for(Discipline discipline: disciplines){
            System.out.println("found discipline: " + discipline);
            listView.setItemChecked((int)discipline.getId()-1, true);
        }
        return view;
    }

    private void displayDisciplinesItems(View view) {
        System.out.println("displaying items");
        dbHandler = new DbHandler(view.getContext(), "", null, 1);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(view.getContext(),
                android.R.layout.simple_list_item_activated_2,
                dbHandler.getAllDisciplines(),
                new String[]{DisciplineContract.DISCIPLINE.COLUMN_NAME, DisciplineContract.DISCIPLINE.COLUMN_RULE},
                new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) view.findViewById(R.id.disciplinesListView);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("destroying view");
        if(dbHandler != null)
            dbHandler.close();
    }
}
