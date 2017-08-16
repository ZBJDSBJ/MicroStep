package com.alost.microstep.presentation.common.utils;

import java.util.Date;

/**
 * Created by chenjingmian on 16/1/13.
 */
public class CustomDate {

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mWeek;
    private DateState mState;

    public enum DateState {
        PAST, TODAY, NEXT
    }

    public CustomDate() {

    }

    public CustomDate(int year, int month, int day) {
        if (month > 12) {
            month -= 12;
            year++;
        } else if (month < 1) {
            month += 12;
            year--;
        }
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
    }

    public CustomDate(int year, int month, int day, int week) {
        if (month > 12) {
            month -= 12;
            year++;
        } else if (month < 1) {
            month += 12;
            year--;
        }
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        this.mWeek = week;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int mYear) {
        this.mYear = mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int mDay) {
        this.mDay = mDay;
    }

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int mWeek) {
        this.mWeek = mWeek;
    }

    public String dateToStr() {
        return mYear + "-" + (mMonth < 10 ? "0" + mMonth : mMonth) + "-" +
                (mDay < 10 ? "0" + mDay : mDay);
    }

    public String dateToStrOther() {
        return mYear + "." + (mMonth < 10 ? "0" + mMonth : mMonth) + "." +
                (mDay < 10 ? "0" + mDay : mDay);
    }

    public DateState getState() {
        return mState;
    }

    public void setState(String today) {
        int interval = DateUtil.getDayInterval(today, this.dateToStr());
        if (interval < 0) {
            this.mState = DateState.PAST;
        } else if (interval == 0) {
            this.mState = DateState.TODAY;
        } else {
            this.mState = DateState.NEXT;
        }
    }

    public DateState getDateState() {
        return getDateState(this.dateToStr());
    }

    public static DateState getDateState(String date) {
        int interval = DateUtil.getDayInterval(DateUtil.getToday().dateToStr(), date);
        if (interval < 0) {
            return DateState.PAST;
        } else if (interval == 0) {
            return DateState.TODAY;
        } else {
            return DateState.NEXT;
        }
    }

    public boolean equals(CustomDate date) {
        if (mYear == date.getYear() && mMonth == date.getMonth() && mDay == date.getDay()) {
            return true;
        } else {
            return false;
        }
    }

    public static CustomDate strToDate(String str) {
        CustomDate customDate = new CustomDate();
        Date date = DateUtils.strToDate(str);
        customDate.setYear(date.getYear() + 1900);
        customDate.setMonth(date.getMonth() + 1);
        customDate.setDay(date.getDate());
        return customDate;
    }

}
