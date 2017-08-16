package com.alost.microstep.presentation.common.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Alost
 *         日历工具类
 */
public class DateUtil {

    public static String[] sMonthName = {"一月", "二月", "三月", "四月", "五月", "六月",
            "七月", "八月", "九月", "十月", "十一月", "十二月"};

    public static String[] sWeekName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static CustomDate getToday() {
        Calendar calendar = Calendar.getInstance();
        CustomDate date = new CustomDate();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setMonth(calendar.get(Calendar.MONTH) + 1);
        date.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 判断当前日历是星期几
     *
     * @param date
     * @return 1-7 对应 周一到周日
     */
    public static int getWeekDay(String date) {
        Date date1 = DateUtils.strToDate(date);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, date1.getYear() + 1900);
        c.set(Calendar.MONTH, date1.getMonth());
        c.set(Calendar.DAY_OF_MONTH, date1.getDate());
        int day = c.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            return 7;
        } else {
            return day - 1;
        }
    }

    /**
     * 获取某天这一周的日期,周一到周日
     *
     * @param date
     * @return
     */
    public static List<CustomDate> getCurrentWeek(String date) {
        int weekDay = DateUtil.getWeekDay(date); // 判断周几
        CustomDate firstDate = DateUtil.getDateChange2(date, -(weekDay - 1)); // 获取周一日期
        int tempDay = firstDate.getDay();
        int currentMonthDays = DateUtil.getMonthDays(firstDate.getYear(), firstDate.getMonth()); // 获取周一这个月的天数
        ArrayList<CustomDate> list = new ArrayList<>();
        CustomDate tempDate;
        for (int i = 0; i < 7; i++) {
            if (tempDay > currentMonthDays) { // 判断是否下个月
                tempDate = new CustomDate(firstDate.getYear(), firstDate.getMonth() + 1,
                        tempDay - currentMonthDays);
            } else {
                tempDate = new CustomDate(firstDate.getYear(), firstDate.getMonth(), tempDay);
            }
            list.add(tempDate);
            tempDay++;
        }
        return list;
    }

    public static String getWeekDay(int week) {
        return sWeekName[week];
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    /**
     * 业务上要从周一开始, 所以用0-6索引表示周一到周日
     *
     * @param year
     * @param month
     * @return
     */
    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK);
        // 系统get(Calendar.DAY_OF_WEEK) 返回数组{1,2,3,4,5,6,7}中用1~7来表示：星期天，星期一，星期二，星期三，星期四，星期五，星期六
        if (week_index == 1) {
            week_index = 6;
        } else {
            week_index = week_index - 2;
        }
        return week_index;
    }

    public static int getWeekCount(int year, int month) {
        int currentMonthDays = DateUtil.getMonthDays(year,
                month); // 当前月的天数
        int firstDayWeek = DateUtil.getWeekDayFromDate(year,
                month);
        return (int) (Math.ceil((currentMonthDays + firstDayWeek) * 1.0f / 7));
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month))
                + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    public static int[] getWeekSunday(int weekChange) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.add(Calendar.WEEK_OF_YEAR, weekChange);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;
    }

    public static int getMonthDuration(int year, int month) {
        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH) + 1;
        return month - curMonth + 12 * (year - curYear);
    }

    /**
     * 判断date2 - date1的天数差, 正数表示date2在date1之后, 负数表示在date1之前
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDayInterval(String date1, String date2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        int day = 0;
        try {
            java.util.Date temp1 = myFormatter.parse(date1);
            java.util.Date temp2 = myFormatter.parse(date2);
            day = (int) ((temp2.getTime() - temp1.getTime()) / (24 * 60 * 60 * 1000));
        } catch (Exception e) {
            return 0;
        }
        return day;
    }

    /**
     * 得到几天前后的时间
     */
    public static CustomDate getDateChange(CustomDate now, int day) {
        return getDateChange2(now.dateToStr(), day);
    }

    /**
     * 得到几天前后的时间
     */
    public static CustomDate getDateChange2(String now, int day) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date date1 = myFormatter.parse(now);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
            CustomDate newDate = new CustomDate();
            newDate.setYear(calendar.get(Calendar.YEAR));
            newDate.setMonth(calendar.get(Calendar.MONTH) + 1);
            newDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            newDate.setWeek(calendar.get(Calendar.DAY_OF_WEEK));
            return newDate;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到月前后的时间
     */
    public static CustomDate geMontChange2(String now, int month) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date date1 = myFormatter.parse(now);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + month);
            CustomDate newDate = new CustomDate();
            newDate.setYear(calendar.get(Calendar.YEAR));
            newDate.setMonth(calendar.get(Calendar.MONTH) + 1);
            newDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            newDate.setWeek(calendar.get(Calendar.DAY_OF_WEEK));
            return newDate;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到几天前后的时间
     */
    public static String getMonthChange(String now, int month) {
        return geMontChange2(now, month).dateToStr();
    }

    /**
     * 得到几天前后的时间
     */
    public static String getDateChange(String now, int day) {
        return getDateChange2(now, day).dateToStr();
    }

    /**
     * 得到几天前后的时间
     */
    public static String getDateChangeShort(String now, int day) {
        return getDateChange2(now, day).dateToStr();
    }


    /**
     * day是否在startDay-endDay之内
     *
     * @param startDay
     * @param endDay
     * @param day
     * @return
     */
    public static boolean isContain(String startDay, String endDay, String day) {
        if (getDayInterval(startDay, day) < 0) { // 在开始时间之前
            return false;
        } else if (getDayInterval(endDay, day) > 0) { // 在结束时间之后
            return false;
        }
        return true;
    }


    /**
     * 返回指定样式的日期
     * 2016/01/01,周一
     */
    public static String getShowDate(CustomDate endDate, String str) {
        String string;
        if (endDate.getMonth() > 9) {
            if (endDate.getDay() > 9) {
                string = endDate.getYear() + "/" + endDate.getMonth() + "/" + endDate.getDay() + str + getWeekDay(endDate.getWeek() - 1);
            } else {
                string = endDate.getYear() + "/" + endDate.getMonth() + "/0" + endDate.getDay() + str + getWeekDay(endDate.getWeek() - 1);
            }
        } else {
            if (endDate.getDay() > 9) {
                string = endDate.getYear() + "/0" + endDate.getMonth() + "/" + endDate.getDay() + str + getWeekDay(endDate.getWeek() - 1);
            } else {
                string = endDate.getYear() + "/0" + endDate.getMonth() + "/0" + endDate.getDay() + str + getWeekDay(endDate.getWeek() - 1);
            }
        }
        return string;
    }


}