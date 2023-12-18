package com.fmbank.welfarelottery.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: DateUtils
 * @Description: 日期工具类
 * @Author: jc
 * @Date: 20211224
 * @Version 1.0
 */
public class DateUtil {

    public static String DATE_PATTERN_YEAR = "yyyy";

    public static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间转时间字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return String
     */
    public static String format(Date date, String pattern) {
        if (date == null)
            return "";
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 时间转时间字符串为yyyy-MM-dd HH:mm:ss 格式
     *
     * @param date 日期
     * @return String
     */
    public static String formatDateTime(Date date) {
        if (date == null)
            return "";
        return DateFormatUtils.format(date, DATE_PATTERN);
    }

    /**
     * 时间字符串转化为yyyy-MM-dd HH:mm:ss 格式
     *
     * @param str 日期
     * @return Date
     */
    public static Date parseDateTimeWithPattern(String str, String pattern) {
        if (str == null)
            return null;
        try {
            return DateUtils.parseDate(str, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转时间
     *
     * @param str             字符串
     * @param dateTimePattern 格式
     * @return Date
     */
    public static Date parseDateTime(String str, String dateTimePattern) {
        if (str == null)
            return null;
        try {
            return DateUtils.parseDate(str, Locale.CHINESE, dateTimePattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当年的第一天
     */
    public static Date getCurrentFirstOfYear() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getFirstOfYear(currentYear);
    }

    /**
     * 获取当年的最后一天
     */
    public static Date getCurrentLastOfYear() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getLastOfYear(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getFirstOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getLastOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 获取某年第一天日期
     *
     * @param date 日期
     * @return Date
     */
    public static Date getFirstOfYear(Date date) {
        int year = Integer.parseInt(String.format("%tY", date));
        return getFirstOfYear(year);
    }

    /**
     * 获取某年最后一天日期
     *
     * @param date 日期
     * @return Date
     */
    public static Date getLastOfYear(Date date) {
        int year = Integer.parseInt(String.format("%tY", date));
        return getLastOfYear(year);
    }

    /**
     * 获取指定日期前一天的日期
     *
     * @param date 日期
     * @return Date
     */
    public static String getLastOfDay(String date)  {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        Date specifiedDate = null;
        try {
            specifiedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(specifiedDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return dateFormat.format(calendar.getTime());
    }
    /**
     * 两日日期相差的天数
     * @param startDate
     * @param endDate
     * @return
     */
    public static Integer getCountOfTwoDay(String startDate,String endDate)  {
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        int i=0;
        try {
            Date star = dft.parse(startDate);//开始时间
            Date endDay=dft.parse(endDate);//结束时间
            Date nextDay=star;
            while(nextDay.before(endDay)){//当明天不在结束时间之前是终止循环
                Calendar cld = Calendar.getInstance();
                cld.setTime(star);
                cld.add(Calendar.DATE, 1);
                star = cld.getTime();
                //获得下一天日期字符串
                nextDay = star;
                i++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static void main(String[] args) {
        System.out.println("=========current year=========");


        System.out.println(getLastOfDay("2019-09-29"));
        Date currentYearStart = getCurrentFirstOfYear();
        System.out.println(formatDateTime(currentYearStart));
        Date currentYearEnd = getCurrentLastOfYear();
        System.out.println(formatDateTime(currentYearEnd));

        Date before = parseDateTimeWithPattern("2013", DATE_PATTERN_YEAR);
        //获取指定年的第一天
        Date beforeYearStart = getFirstOfYear(before);
        System.out.println(formatDateTime(beforeYearStart));
        //获取指定年的第一天的最后一天
        Date beforeEnd = getLastOfYear(before);
        System.out.println(formatDateTime(beforeEnd));
    }

}
