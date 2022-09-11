package com.capstone.projecttea;

import android.text.format.Time;

public class Utils {

    public Utils() {

    }

    public static Object CheckTextIfNull(Object object) {
        if (object == null) {
            return object.toString().trim();
        }
        return object;
    }

    public static String TimeAndDateNow(String format) {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int month = today.month + 1;
        int day = today.monthDay;
        int year = today.year;
        String time = today.format("%k%M%S");

         if(format.equals("Date")){
            return month+""+day+""+year;
        }
        else if(format.equals("Time")){
            return time;
        }
        return month+""+day+""+year+""+time;

    }
}
