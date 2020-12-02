package com.search.common.utils;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 */
public class DateUtils  extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] PARSE_PATTERNS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM" };

    /**
     * 获取当前Date型日期 yyyymmdd HH:mm:ss
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前Date型日期,yyyy-mm-dd
     */
    public static Date getNowDateOnly() {
        try {
            return new SimpleDateFormat(YYYY_MM_DD).parse(getDate());
        } catch (ParseException e) {
            return getNowDate();
        }
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), PARSE_PATTERNS);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算相差的小时
     *
     * @param fromDate 从一个时间时间小的
     * @param toDate   到一个时间 大的
     * @return 返回小时数
     */
    public static Long getDatePoorHour(Date fromDate, Date toDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long diff = toDate.getTime() - fromDate.getTime();
        return diff % nd / nh;
    }

    /**
     * 计算相差周
     * @param start 开始时间
     * @param end 结束时间
     * @return 相差数目
     */
    public static Integer getDatePoorWeek(Date start, Date end) {
        int twoDateDays = getTwoDateDays(start, end);
        int week = twoDateDays / 7;
        long asd = twoDateDays % 7;
        if (asd != 0) {
            week += 1;
        }
        return Integer.valueOf(String.valueOf(week));
    }

    public static Integer getTwoDateDays(Date start, Date end) {
        long timeStamp = end.getTime() - start.getTime();
        long betweenDate = (timeStamp) / (60 * 60 * 24 * 1000);
        long nd = 1000 * 24 * 60 * 60;
        long hour = timeStamp % nd;
        if (hour != 0) {
            betweenDate += 1;
        }
        return Integer.valueOf(String.valueOf(betweenDate));
    }

    public static Integer getTwoDateMonth(Date start, Date end) {
        int twoDateDays = getTwoDateDays(start, end);
        int month = twoDateDays / 30;
        long asd = twoDateDays % 30;
        if (asd != 0) {
            month += 1;
        }
        return Integer.valueOf(String.valueOf(month));
    }

    public static void main(String[] args) {
        Date start = dateTime(YYYY_MM_DD, "2020-01-01");
        Date end = dateTime(YYYY_MM_DD, "2020-01-31");
        Date months = DateUtils.addMonths(start, 1);
        Date addDays = DateUtils.addDays(months, -1);
        if(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, end).equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, addDays))) {
//			| return false;
            System.out.println("a");
        }else {
            System.out.println("2");
        }
    }

    public static Date addDateByNumberDay(Date date, int numberDay) {
        return addDays(date, numberDay);
    }

    public static Date getCurDateStart(int number) throws ParseException {
        Date date = getNowDate();
        Date addDays = addDays(date, -number);
        SimpleDateFormat fa = new SimpleDateFormat(YYYY_MM_DD);
        return fa.parse(parseDateToStr(YYYY_MM_DD_HH_MM_SS, addDays));
    }

    public static Date getCurDateEnd() throws ParseException {
        Date date = getNowDate();
        Date addDays = addDays(date, 1);
        SimpleDateFormat fa = new SimpleDateFormat(YYYY_MM_DD);
        return fa.parse(parseDateToStr(YYYY_MM_DD_HH_MM_SS, addDays));
    }

    /**
     * 获取当前日期跟明天凌晨（00:00:00）相差的秒数
     *
     */
    public static Long getLessTomorrowSecond() {
        Long tomorrowLong = DateUtil.beginOfDay(DateUtil.tomorrow()).getTime();
        Long nowLong = DateUtil.date().getTime();
        return (tomorrowLong - nowLong) / 1000;
    }
}
