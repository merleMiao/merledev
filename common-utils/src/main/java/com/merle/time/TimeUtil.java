package com.merle.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static Date add(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return calendar.getTime();
    }

    public static String format(Date date, String type) {
        SimpleDateFormat s = new SimpleDateFormat(type);
        String t = "";
        try {
            t = s.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String sFormat(Date date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        String t = "";
        try {
            t = s.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String lFormat(Date date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String t = "";
        try {
            t = s.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static Date parse(String date, String type) {
        SimpleDateFormat s = new SimpleDateFormat(type);
        Date t = null;
        try {
            t = s.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static Date lParse(String date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date t = null;
        try {
            t = s.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static Date sParse(String date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date t = null;
        try {
            t = s.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static int year() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static int quarter() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return (int) Math.ceil((double) month / 3);
    }

    public static int month() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    public static int week() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(2);
        int month = calendar.get(Calendar.MONTH) + 1;
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (week == 1 && month != 1) {
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            week = calendar.get(Calendar.WEEK_OF_YEAR) + 1;
        }
        return week;
    }

    public static String day() {
        return lFormat(new Date());
    }


    public static Date getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month - 1, 1, 0, 0, 0);
        return calendar.getTime();
    }

    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(Calendar.MONTH, month - 1);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(year, month - 1, day, 23, 59, 59);
        return calendar.getTime();
    }

    public static Date getFirstDayOfQuarter(Integer year, int quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 1;
        } else if (quarter == 2) {
            month = 4;
        } else if (quarter == 3) {
            month = 7;
        } else if (quarter == 4) {
            month = 10;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDayOfMonth(year, month);
    }

    public static Date getLastDayOfLastQuarter(Integer year, int quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 3;
        } else if (quarter == 2) {
            month = 6;
        } else if (quarter == 3) {
            month = 9;
        } else if (quarter == 4) {
            month = 12;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    public static Date getFirstDayOfWeek(Integer year, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.WEEK_OF_YEAR, 1);
        calendar.setFirstDayOfWeek(1);

        calendar.set(Calendar.DAY_OF_WEEK, 2);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DAY_OF_YEAR, (week - 1) * 7);
        return calendar.getTime();
    }

    public static Date getLastDayOfLastWeek(Integer year, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.WEEK_OF_YEAR, 1);
        calendar.setFirstDayOfWeek(1);

        calendar.set(Calendar.DAY_OF_WEEK, 2);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.SECOND, -1);

        calendar.add(Calendar.DAY_OF_YEAR, week * 7);
        return calendar.getTime();
    }


    public static void main(String args[]) {


    }
}