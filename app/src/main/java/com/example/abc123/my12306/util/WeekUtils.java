package com.example.abc123.my12306.util;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeekUtils {
    public static String getWeek(String pTime) {
        String week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            week += "周日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            week += "周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            week += "周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            week += "周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            week += "周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            week += "周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            week += "周六";
        }
        return week;
    }
}
