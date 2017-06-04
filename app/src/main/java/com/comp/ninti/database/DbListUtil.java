package com.comp.ninti.database;


import com.comp.ninti.general.Discipline;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DbListUtil {
    private static String LIST_SEPARATOR = ",";

    public static String convertListToString(List<Long> stringList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Long lon : stringList) {
            stringBuilder.append(lon).append(LIST_SEPARATOR);
        }

        // Remove last separator
        int lastIndex = stringBuilder.lastIndexOf(LIST_SEPARATOR);
        stringBuilder.delete(lastIndex, lastIndex + LIST_SEPARATOR.length() + 1);

        return stringBuilder.toString();
    }

    public static LinkedList<Long> convertDisciplinesToList(List<Discipline> disciplines){
        LinkedList<Long> longDisc = new LinkedList<>();
        for(Discipline discipline: disciplines){
            longDisc.add(discipline.getId());
        }
        return longDisc;
    }

    public static List<String> convertStringToList(String str) {
        return Arrays.asList(str.split(LIST_SEPARATOR));
    }
}
