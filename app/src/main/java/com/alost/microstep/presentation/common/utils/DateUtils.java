package com.alost.microstep.presentation.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * @author Alost
 *         时间工具类
 */
public class DateUtils {


    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    //年月日 转换成 月日
    public static String toChangeTimeFormat(String strDate) {
        String result = StringToDate(strDate, "yyyy-MM-dd", "MM-dd");

        return result;
    }

    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss  *   * @param dateDate  * @return
     */
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     */
    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy.MM.dd
     *
     * @param dateDate
     */
    public static String dateToStr2(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);

        return strtodate;
    }

    //年月日 转换成 xx月xx
    public static String toChangeTimeFormat1(String strDate) {
        String result = StringToDate(strDate, "yyyy-MM-dd", "MM月dd");

        return result;
    }

    //年月日 转换成 日
    public static String toChangeTimeFormat2(String strDate) {
        String result = StringToDate(strDate, "yyyy-MM-dd", "dd");

        return result;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy.MM.dd  HH:mm
     */
    public static String getStrFromTime(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm");//初始化Formatter的转换格式。
        String dateString = formatter.format(time);

        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间  yyyyMMdd
     */
    public static String getStrFromTimeShort(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");//初始化Formatter的转换格式。
        String dateString = formatter.format(time);

        return dateString;
    }


    /**
     * 将长时间格式字符串转换为时间 MM月dd日 HH:mm:ss
     */
    public static String getStrFromTimeOther(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");//初始化Formatter的转换格式。
        String dateString = formatter.format(time);

        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 MM月dd日
     */
    public static String getMonthDay(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");//初始化Formatter的转换格式。
        String dateString = formatter.format(time);

        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd
     */
    public static String getStrOtherFromTime(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//初始化Formatter的转换格式。
        String dateString = formatter.format(time);

        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 HH:mm
     */
    public static String getHoursFromTime(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");//初始化Formatter的转换格式。
        String dateString = formatter.format(time);

        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm
     *
     * @param time
     */
    public static String getYearDateFromTime(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//初始化Formatter的转换格式。
        String dateString = formatter.format(time);

        return dateString;
    }

    public static String getTotalTime(Integer time) {
        long sportTime = time - TimeZone.getDefault().getRawOffset();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
        String hms = formatter.format(sportTime);
        return hms;
    }

    //获取时分秒分隔值
    public static String[] getTotalTimeDivide(Integer time) {
        String[] timeDivides = new String[3];
        long sportTime = time - TimeZone.getDefault().getRawOffset();
        SimpleDateFormat formatter = new SimpleDateFormat("HH");//初始化Formatter的转换格式。
        String hms = formatter.format(sportTime);
        timeDivides[0] = hms;
        formatter = new SimpleDateFormat("mm");
        String hms2 = formatter.format(sportTime);
        timeDivides[1] = hms2;
        formatter = new SimpleDateFormat("ss");
        String hms3 = formatter.format(sportTime);
        timeDivides[2] = hms3;
        return timeDivides;
    }

    //获取两位数的字符串
    public static String getStringFromDouble(double value) {
        return String.format("%.02f", value);
    }

    //获取1位数的字符串
    public static String getOneStringFromDouble(double value) {

        return String.format("%.01f", value);
    }

    //获取0位数的字符串
    public static String getZeroStringFromDouble(double value) {

        return String.format("%.0f", value);
    }

    /**
     * 小数点处理，保留一个小数点
     */
    public static float decimalProcess(double data) {
        return Math.round(data * 10.0f) / 10.0f;
    }


    // 通过毫秒获取 获取年
    public static String getYear(Long endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endTime);
        int year = calendar.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    // 通过毫秒获取 获取月
    public static String getMonth(Long endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endTime);
        int month = calendar.get(Calendar.MONTH) + 1;
        return String.valueOf(month);
    }

    // 通过毫秒获取 获取日
    public static String getDay(Long endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endTime);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(day);
    }

    public static CustomDate getDate(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        CustomDate date = new CustomDate();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setMonth(calendar.get(Calendar.MONTH) + 1);
        date.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        date.setWeek(calendar.get(Calendar.DAY_OF_WEEK));
        return date;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static String longStrToShortDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);

        return dateToStr(strtodate);
    }


    public static String getStrToDynamicTime(String strTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long publishTime = 0;
        try {
            Date publicDate = format.parse(strTime);
            publishTime = publicDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return getDynamicTime(publishTime);
    }

    /**
     * 动态 时间转化显示
     */
    public static String getDynamicTime(long publishTime) {
        //1:发布时间
        String timeStr;
        //2:比较
        timeStr = DateUtils.getStrOtherFromTime(publishTime);//yyyy-MM-dd
        if (timeStr.equals(DateUtils.getStringDateShort())) {
            timeStr = "今天 " + DateUtils.getHoursFromTime(publishTime);
        } else if (timeStr.equals(DateUtil.getDateChangeShort(DateUtils.getStringDateShort(), -1))) {
            timeStr = "昨日 " + DateUtils.getHoursFromTime(publishTime);
        } else if (DateUtils.getYear(publishTime).equals(String.valueOf(DateUtil.getYear()))) {
            timeStr = DateUtils.getStrFromTimeOther(publishTime);
        } else {
            timeStr = DateUtils.getYearDateFromTime(publishTime);
        }
        return timeStr;
    }

    public static String getPemeterTime(String start_time) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(start_time, pos);

        String timeStr = dateToStr(strtodate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(strtodate.getTime());
        int year = calendar.get(Calendar.YEAR);

        if (timeStr.equals(DateUtils.getStringDateShort())) {
            timeStr = "今天 ";
        } else if (timeStr.equals(DateUtil.getDateChangeShort(DateUtils.getStringDateShort(), -1))) {
            timeStr = "昨日 ";
        } else if (year == DateUtil.getYear()) {
            timeStr = DateUtils.getMonthDay(strtodate.getTime());
        } else {
            timeStr = DateUtils.getStrOtherFromTime(strtodate.getTime());
        }

        return timeStr;
    }

    public static String showCreateTime(String startTime, String createTime) {

        if (startTime == null || startTime.equals("")) {
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            ParsePosition pos2 = new ParsePosition(0);
            Date createDate = formatter2.parse(createTime, pos2);
            return getStrToDynamicTime(createTime);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date startDate = formatter.parse(startTime, pos);

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos2 = new ParsePosition(0);
        Date createDate = formatter2.parse(createTime, pos2);

        //如果开始时间与创建时间一样，返回创建时间
        if (startDate.getTime() == createDate.getTime()) {
            return getStrToDynamicTime(createTime);
        } else {
            return dateToStr(startDate);
        }

    }
}
