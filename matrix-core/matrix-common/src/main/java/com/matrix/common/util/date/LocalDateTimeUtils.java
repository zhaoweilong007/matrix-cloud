package com.matrix.common.util.date;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间工具类，用于 {@link LocalDateTime}
 */
public class LocalDateTimeUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 空的 LocalDateTime 对象，主要用于 DB 唯一索引的默认值
     */
    public static LocalDateTime EMPTY = buildTime(1970, 1, 1);

    public static LocalDateTime addTime(Duration duration) {
        return LocalDateTime.now().plus(duration);
    }

    public static boolean beforeNow(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    public static boolean afterNow(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now());
    }

    /**
     * 创建指定时间
     *
     * @param year  年
     * @param mouth 月
     * @param day   日
     * @return 指定时间
     */
    public static LocalDateTime buildTime(int year, int mouth, int day) {
        return LocalDateTime.of(year, mouth, day, 0, 0, 0);
    }

    public static LocalDateTime[] buildBetweenTime(int year1, int mouth1, int day1,
                                                   int year2, int mouth2, int day2) {
        return new LocalDateTime[]{buildTime(year1, mouth1, day1), buildTime(year2, mouth2, day2)};
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        return LocalDateTimeUtil.isIn(LocalDateTime.now(), startTime, endTime);
    }

    // 获取当前日期时间
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    // 获取当前日期
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    // 获取当前时间
    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    // 格式化日期时间为字符串
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DEFAULT_DATE_TIME_FORMAT);
    }

    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    // 解析字符串为日期时间
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DEFAULT_DATE_TIME_FORMAT);
    }

    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    // 格式化日期为字符串
    public static String formatDate(LocalDate date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    public static String formatDate(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    // 解析字符串为日期
    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    public static LocalDate parseDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * 将日期转为中文格式
     * 例如：11:30 转为上午11点30分
     * 13:30 转成 下午1点30分
     */
    public static String convertTime(String timeString) {
        LocalTime time = LocalTime.parse(timeString);
        String hour = time.getHour() > 12 ? ("下午" + (time.getHour() - 12)) : (time.getHour() == 12 ? "中午12" : "上午" + time.getHour());
        return hour.concat("点").concat((time.getMinute() == 0 ? "" : time.getMinute() + "分"));
    }

    /**
     * 将日期转为中文格式
     * 例如：yyyy-MM-dd转成yyyy年mm月dd日
     * yyyy是本年的去掉年，只显示mm月dd日
     */
    public static String formatDateString(String dateString) {
        return dateString.replaceFirst("-", "年").replaceFirst("-", "月").concat("日")
                .replaceFirst(LocalDateTime.now().getYear() + "年", "");
    }

    public static Date convertToDate(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant2 = zonedDateTime.toInstant();
        return Date.from(instant2);
    }
}
