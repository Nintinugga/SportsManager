package com.comp.ninti.general;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static SimpleDateFormat isoFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS", Locale.getDefault());
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static String getDateTime(int year, int month, int day, int hours, int minutes) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int getPointsFromTime(int maxP, int minP, double maxT, double minT, int t) {
        if (t < minT)
            return maxP;
        if (t > maxT)
            return minP;
        return (int) (((maxP - minP) * (1 - (t - minT) / (maxT - minT)) + minP) + 0.5);
    }

}
