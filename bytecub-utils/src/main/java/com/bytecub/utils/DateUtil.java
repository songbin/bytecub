package com.bytecub.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * 有关 <code>Date</code> 处理的工具类
 *
 * @author hanxiaoqiang
 */
public class DateUtil {

    public final static long ONE_DAY_SECONDS = 86400;
    public final static String shortFormat = "yyyyMMdd";
    public final static String longFormat = "yyyyMMddHHmmss";
    public final static String webFormat = "yyyy-MM-dd";
    public final static String webMonth = "yyyy-MM";
    public final static String webDay = "MM-dd";
    public final static String timeFormat = "HHmmss";
    public final static String monthFormat = "yyyyMM";
    public final static String chineseDtFormat = "yyyy年MM月dd日";
    public final static String newFormat = "yyyy-MM-dd HH:mm:ss";
    public final static String newDayFormat = "yyyy-MM-dd 00:00:00";
    public final static String endDayFormat = "yyyy-MM-dd 23:59:59";
    public final static String noSecondFormat = "yyyy-MM-dd HH:mm";
    public final static String dateTimeWithZone = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    public final static String dateTimeWithZoneZ = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public final static String dayFormat = "yyyy/MM/dd";
    public final static String yearFormat = "yyyy";
    public final static String month = "MM";
    public final static String second = "ss";
    public final static String timeFormat2 = "HH:mm:ss";
    public final static String timeFormat_HH_MM = "HH:mm";
    public final static String TOMORROW_CH = "明天";
    public final static long ONE_DAY_MILL_SECONDS = 86400000;
    public final static String currentTimeZone = "GMT+8";
    public final static String chinaTime = "MM时:mm分:ss秒";
    public final static String chinaDate = "yyyy年MM月dd日 HH时:mm分:ss秒";
    public final static String[] weekArr = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public final static Integer DAYS_A_MONTH = 30;
    public final static Integer DAYS_A_WEEK = 7;
    public final static Integer DAYS_THREE_MONTHS = DAYS_A_MONTH * 3;


    public static String formatDateTimeWithZone(Date date) {
        return DateUtil.format(date, dateTimeWithZone) + ":00";
    }
    /**
     * 获取当前时间的指定格式
     *
     * @param pattern
     * @return String
     */
    public static String getNow(String pattern) {
        return formatDate(Calendar.getInstance(), pattern);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param pattern
     * @return String
     */
    public static Date getDate(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 日期转字符串
     *
     * @param calendar 日历类型
     * @param pattern  格式字符串
     * @return String 格式化后的字符串
     */
    public static String formatDate(Calendar calendar, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = webFormat;
        }
        return DateFormatUtils.format(calendar, pattern);
    }

    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);

        df.setLenient(false);
        return df;
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat(format).format(date);
    }

    public static Date parseDateNoTime(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(shortFormat);

        if ((sDate == null) || (sDate.length() < shortFormat.length())) {
            throw new ParseException("length too little", 0);
        }

        if (!StringUtils.isNumeric(sDate)) {
            throw new ParseException("not all digit", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static Date parseDateNoTime(String sDate, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("Null format. ", 0);
        }

        DateFormat dateFormat = new SimpleDateFormat(format);

        if ((sDate == null) || (sDate.length() < format.length())) {
            throw new ParseException("length too little", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static Date parseDateNoTimeWithDelimit(String sDate, String delimit)
            throws ParseException {
        sDate = sDate.replaceAll(delimit, "");

        DateFormat dateFormat = new SimpleDateFormat(shortFormat);

        if ((sDate == null) || (sDate.length() != shortFormat.length())) {
            throw new ParseException("length not match", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static Date parseDateLongFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(longFormat);
        Date d = null;

        if ((sDate != null) && (sDate.length() == longFormat.length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }

        return d;
    }

    public static Date parseDateNewFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(newFormat);
        Date d = null;
        if ((sDate != null) && (sDate.length() == newFormat.length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }
        return d;
    }



    public static String getStringByFormat(Date date, String format) {
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算当前时间几小时之后的时间
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, long hours) {
        return addMinutes(date, hours * 60);
    }

    /**
     * 计算当前时间几分钟之后的时间
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, minutes * 60);
    }

    /**
     * @param date1
     * @param secs
     * @return
     */

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + (secs * 1000));
    }

    /**
     * 判断输入的字符串是否为合法的小时
     *
     * @param hourStr
     * @return true/false
     */
    public static boolean isValidHour(String hourStr) {
        if (!StringUtils.isEmpty(hourStr) && StringUtils.isNumeric(hourStr)) {
            int hour = new Integer(hourStr).intValue();

            if ((hour >= 0) && (hour <= 23)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断输入的字符串是否为合法的分或秒
     *
     * @param
     * @return true/false
     */
    public static boolean isValidMinuteOrSecond(String str) {
        if (!StringUtils.isEmpty(str) && StringUtils.isNumeric(str)) {
            int hour = new Integer(str).intValue();

            if ((hour >= 0) && (hour <= 59)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 取得新的日期
     *
     * @param date1 日期
     * @param days  天数
     * @return 新的日期
     */
    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, days * ONE_DAY_SECONDS);
    }

    public static String getTomorrowDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);

        aDate = addSeconds(aDate, ONE_DAY_SECONDS);

        return getDateString(aDate);
    }

    public static String getLongDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(longFormat);

        return getDateString(date, dateFormat);
    }

    public static String getNewFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(newFormat);
        return getDateString(date, dateFormat);
    }

    public static String getNewDayFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(newDayFormat);
        return getDateString(date, dateFormat);
    }

    public static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    public static String getYesterDayDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);

        aDate = addSeconds(aDate, -ONE_DAY_SECONDS);

        return getDateString(aDate);
    }

    /**
     * @return 当天的时间格式化为"yyyyMMdd"
     */
    public static String getDateString(Date date) {
        DateFormat df = getNewDateFormat(shortFormat);

        return df.format(date);
    }

    /**
     * 转换为YYYY-MM-DD
     *
     * @param date
     * @return
     */
    public static String getWebDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(webFormat);

        return getDateString(date, dateFormat);
    }

    /**
     * 转换为YYYY-MM-
     *
     * @param date
     * @return
     */
    public static String getWebMonthString(Date date) {
        DateFormat dateFormat = getNewDateFormat(webMonth);

        return getDateString(date, dateFormat);
    }


    /**
     * 取得“X年X月X日”的日期格式
     *
     * @param date
     * @return
     */
    public static String getChineseDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(chineseDtFormat);

        return getDateString(date, dateFormat);
    }

    public static String getTodayString() {
        DateFormat dateFormat = getNewDateFormat(shortFormat);

        return getDateString(new Date(), dateFormat);
    }

    public static String getTimeString(Date date) {
        DateFormat dateFormat = getNewDateFormat(timeFormat);

        return getDateString(date, dateFormat);
    }

    public static String getBeforeDayString(int days) {
        Date date = new Date(System.currentTimeMillis() - (ONE_DAY_MILL_SECONDS * days));
        DateFormat dateFormat = getNewDateFormat(shortFormat);

        return getDateString(date, dateFormat);
    }

    /**
     * 取得两个日期间隔秒数（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔秒数
     */
    public static long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
    }

    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (60 * 1000);
    }

    /**
     * 取得两个日期的间隔天数
     *
     * @param one
     * @param two
     * @return 间隔天数
     */
    public static long getDiffDays(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);
    }

    public static String getBeforeDayString(String dateString, int days) {
        Date date;
        DateFormat df = getNewDateFormat(shortFormat);

        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            date = new Date();
        }

        date = new Date(date.getTime() - (ONE_DAY_MILL_SECONDS * days));

        return df.format(date);
    }

    public static boolean isValidShortDateFormat(String strDate) {
        if (strDate.length() != shortFormat.length()) {
            return false;
        }

        try {
            //---- 避免日期中输入非数字 ----
            Integer.parseInt(strDate);
        } catch (Exception NumberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(shortFormat);

        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidShortDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidShortDateFormat(temp);
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     *
     * @param strDate
     * @return
     */
    public static boolean isValidLongDateFormat(String strDate) {
        if (strDate.length() != longFormat.length()) {
            return false;
        }

        try {
            //---- 避免日期中输入非数字 ----
            Long.parseLong(strDate);
        } catch (Exception NumberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(longFormat);

        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     *
     * @param strDate
     * @param delimiter
     * @return
     */
    public static boolean isValidLongDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidLongDateFormat(temp);
    }

    public static String getShortDateString(String strDate) {
        return getShortDateString(strDate, "-|/");
    }

    public static String getShortDateString(String strDate, String delimiter) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        String temp = strDate.replaceAll(delimiter, "");

        if (isValidShortDateFormat(temp)) {
            return temp;
        }

        return null;
    }

    public static String getShortFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat df = getNewDateFormat(shortFormat);

        return df.format(cal.getTime());
    }

    /**
     * 根据日期获取 当月第一天的时间格式
     *
     * @param date
     * @return
     */
    public static String getShortFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat df = getNewDateFormat(shortFormat);

        return df.format(cal.getTime());
    }

    public static String getWebTodayString() {
        DateFormat df = getNewDateFormat(webFormat);

        return df.format(new Date());
    }

    public static String getWebFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat df = getNewDateFormat(webFormat);

        return df.format(cal.getTime());
    }

    public static String convert(String dateString, DateFormat formatIn, DateFormat formatOut) {
        try {
            Date date = formatIn.parse(dateString);

            return formatOut.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static String convert2WebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(webFormat);

        return convert(dateString, df1, df2);
    }

    public static String convert2ChineseDtFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(chineseDtFormat);

        return convert(dateString, df1, df2);
    }

    public static String convertFromWebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(webFormat);

        return convert(dateString, df2, df1);
    }

    public static boolean webDateNotLessThan(String date1, String date2) {
        DateFormat df = getNewDateFormat(webFormat);

        return dateNotLessThan(date1, date2, df);
    }

    /**
     * @param date1
     * @param date2
     * @param
     * @return
     */
    public static boolean dateNotLessThan(String date1, String date2, DateFormat format) {
        try {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);

            if (d1.before(d2)) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getEmailDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

        todayStr = sdf.format(today);
        return todayStr;
    }

    public static String getSmsDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");

        todayStr = sdf.format(today);
        return todayStr;
    }

    public static String formatMonth(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(monthFormat).format(date);
    }

    /**
     * 获取系统日期的前一天日期，返回Date
     *
     * @return
     */
    public static Date getBeforeDate() {
        Date date = new Date();

        return new Date(date.getTime() - (ONE_DAY_MILL_SECONDS));
    }

    /**
     * 获取参数的前一天日期，返回Date
     *
     * @return
     */
    public static String getBeforeDateStr(String dateStr) {
        DateFormat df = new SimpleDateFormat(longFormat);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(date);
        sysDate.add(Calendar.DAY_OF_YEAR, -1);
        return df.format(sysDate.getTime());
    }

    /**
     * 获得指定时间当天起点时间
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);

        String dateString = df.format(date);

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 判断参date上min分钟后，是否小于当前时间
     *
     * @param date
     * @param min
     * @return
     */
    public static boolean dateLessThanNowAddMin(Date date, long min) {
        return addMinutes(date, min).before(new Date());

    }

    public static boolean isBeforeNow(Date date) {
        if (date == null) {
            return false;
        }

        return date.compareTo(new Date()) < 0;
    }

    public static Date parseNoSecondFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(noSecondFormat);

        if ((sDate == null) || (sDate.length() < noSecondFormat.length())) {
            throw new ParseException("length too little", 0);
        }

        if (!StringUtils.isNumeric(sDate)) {
            throw new ParseException("not all digit", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static Date addMonth(Date date, int months) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(date);
        sysDate.add(Calendar.MONTH, months);
        return sysDate.getTime();
    }

    public static Date getNewDateByFormate(Date date, String formate) {
        DateFormat df = new SimpleDateFormat(formate);

        String dateString = df.format(date);

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 获取N个月后的年月格式日期
     *
     * @param date
     * @param months
     * @return
     */
    public static String addMonthExtend(Date date, int months) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(date);
        sysDate.add(Calendar.MONTH, months);
        return formatMonth(sysDate.getTime());
    }

    public static String addYearExtend(Date date, int year) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(date);
        sysDate.add(Calendar.YEAR, year);
        return formatYear(sysDate.getTime());
    }

    public static String formatYear(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(yearFormat).format(date);
    }

    /**
     * 获取指定相隔N周的星期一
     *
     * @return
     */
    public static String getWeekMonday(int temp, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //n为推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
        String monday;
        cal.add(Calendar.DATE, temp * 7);
        //想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        monday = new SimpleDateFormat(shortFormat).format(cal.getTime());
        return monday;
    }

    /**
     * 获取指定相隔N周的周日
     *
     * @return
     */
    public static String getWeekSunday(int temp, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        //n为推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
        String monday;
        cal.add(Calendar.DATE, temp * 7);
        //想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        monday = new SimpleDateFormat(shortFormat).format(cal.getTime());
        return monday;
    }

    /**
     * @param date      基准时间
     * @param dayOfWeek 星期几
     * @return
     */
    public static String getWeekTargetDay(int weekOfYeah, Date date, int dayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //n为推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
        String monday;
        cal.add(Calendar.DATE, weekOfYeah * 7);
        //周内时间星期几对应的date
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        monday = new SimpleDateFormat(shortFormat).format(cal.getTime());
        return monday;
    }

    /**
     * 计算两个日期之间月份的间隔数
     */
    public static int getDifferenceMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 将PHP中时间转换成java中的date.
     *
     * @param time    Long类型的时间
     * @param pattern 时间格式
     * @return Date
     */
    public static Date convertDate(Long time, String pattern) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            date = sdf.parse(sdf.format(new Date(time * 1000)));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static String isOverTime(String beginDate, String endDate, String formate) {
        if (StringUtils.isBlank(formate)) {
            return "日期格式错误";
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            return "日期错误";
        }

        try {
            Date begin = DateUtil.parseDateNoTime(beginDate, formate);
            if (getDiffSeconds(new Date(), begin) < 0) {
                return "未开始";
            }
        } catch (Exception e) {
            return "日期转换错误";
        }


        try {
            Date end = DateUtil.parseDateNoTime(endDate, formate);
            if (getDiffSeconds(new Date(), end) > 0) {
                return "已结束";
            }
        } catch (Exception e) {
            return "日期转换错误";
        }


        try {
            Date begin = DateUtil.parseDateNoTime(beginDate, formate);
            Date end = DateUtil.parseDateNoTime(endDate, formate);
            if (getDiffSeconds(new Date(), begin) >= 0 && getDiffSeconds(new Date(), end) <= 0) {
                return "进行中";
            }
        } catch (Exception e) {
            return "日期转换错误";
        }

        return "";
    }

    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static boolean verifyStartEndTime(String startTime, String endTime) {
        // 如果传有时间参数进行判断
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            Date start = parseDateNewFormat(startTime);
            Date end = parseDateNewFormat(endTime);
            if (start == null || end == null || end.getTime() < start.getTime()) {

                throw new RuntimeException("时间区间有误");
            }
        }
        return true;
    }

    /**
     * 将LocalDate转化为Date
     *
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 将Date转化为LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        return zonedDateTime.toLocalDate();

    }

    /**
     * 将LocalDateTime转化为Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 将Date转化为LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, zoneId);

    }

    /**
     * 获取一天开始时间 如 2014-12-12 00:00:00
     *
     * @return 一天开始时间
     */
    public static Date getDayStart(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天结束时间 如 2014-12-12 23:59:59
     *
     * @return 一天结束时间
     */
    public static Date getDayEnd(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @author: gaojing [gaojing1996@vip.qq.com]
     */
    public static boolean inSameDay(Date one, Date two) {
        return format(one, webFormat).equals(format(two, webFormat));
    }



    public static String getHttpDateHeaderValue(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(date);
    }

    public static boolean isEvening() {
        Date now = new Date();
        int[] hours = {21, 22, 23, 0, 1, 2, 3, 4};
        for (int hour : hours) {
            if (hour == now.getHours()) {
                return true;
            }
        }
        return false;
    }

    public static int getSecondsToEndOfToday() {
        Date now = new Date();
        long diffSeconds = getDiffSeconds(getDayEnd(now), now);
        if (diffSeconds < 0) {
            return 0;
        }
        return (int) diffSeconds;
    }
    /**存入es的数据都是UTC标准时间*/
    public static String formatForEs(Date date) {
        if(null == date){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.dateTimeWithZoneZ);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = dateFormat.format(date) + "Z";
        return dateStr;
    }
    /**存入es的数据都是UTC标准时间*/
    public static String formatForEsGMT8(Date date) {
        if(null == date){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.dateTimeWithZoneZ);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String dateStr = dateFormat.format(date) + "Z";
        return dateStr;
    }
    public static void main(String[] args) throws Exception {
//        while (true) {
//            System.out.println(getSecondsToEndOfToday());
//            Thread.sleep(1000);
//        }
        Date date = new Date();
        System.out.println(formatForEs(date));
    }
}
