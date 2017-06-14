package com.comp.ninti.general;

import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

public class SelectionUtil {
    public static boolean selectAll(ListView listView) {
        if (!isSelectAllSelectable(listView))
            return false;

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            listView.setItemChecked(i, true);
        }
        return true;
    }

    public static boolean deselectAll(ListView listView) {
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            listView.setItemChecked(i, false);
        }
        return true;
    }

    private static boolean isSelectAllSelectable(ListView listView) {
        if (listView.getChoiceMode() == AbsListView.CHOICE_MODE_NONE || listView.getChoiceMode() == AbsListView.CHOICE_MODE_SINGLE) {
            Toast.makeText(listView.getContext(), "Not supported for this list", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
