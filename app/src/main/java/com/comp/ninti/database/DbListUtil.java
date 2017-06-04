package com.comp.ninti.database;


import java.util.Arrays;
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

    public static List<String> convertStringToList(String str) {
        return Arrays.asList(str.split(LIST_SEPARATOR));
    }
}
