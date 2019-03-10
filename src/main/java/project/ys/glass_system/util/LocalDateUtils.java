package project.ys.glass_system.util;

import org.apache.tomcat.jni.Local;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateUtils {
    //默认使用系统当前时区
    public static final ZoneId ZONE = ZoneId.systemDefault();

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_FORMAT_CN = "yyyy年MM月dd日 ";


    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String REGEX = "\\:|\\-|\\s";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将Date转换成LocalDateTime
     *
     * @param date Date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime;
    }

    /**
     * 将Date转换成LocalDate
     *
     * @param date Date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalDate();
    }

    /**
     * 将Date转换成LocalTime
     *
     * @param date Date
     * @return
     */
    public static LocalTime dateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalTime();
    }

    /**
     * 将LocalDate转换成Date
     *
     * @param localDate
     * @return date
     */
    public static Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 将LocalDateTime转换成Date
     *
     * @param localDateTime
     * @return date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }


    public static String dateToStr(LocalDate date, String format) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(format);
        return date.format(formatters);
    }

    public static String dateToStr(LocalDateTime date, String format) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(format);
        return date.format(formatters);
    }


    public static LocalDate strToDate(String str, String format) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(str, formatters);
    }

    /**
     * 将相应格式yyyy-MM-dd yyyy-MM-dd HH:mm:ss 时间字符串转换成Date
     *
     * @param time   string
     * @param format string
     * @return date
     */
    public static Date stringToDate(String time, String format) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
        if (DATE_TIME_FORMAT.equals(format)) {
            return LocalDateUtils.localDateTimeToDate(LocalDateTime.parse(time, f));
        } else if (DATE_FORMAT.equals(format)) {
            return LocalDateUtils.localDateToDate(LocalDate.parse(time, f));
        }
        return null;
    }

    /**
     * 将String任意时间转换成当天LocalDateTime格式
     *
     * @param time string
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String time) {
        return LocalDateTime.parse(dateToStr(LocalDate.now(), DATE_FORMAT) + "T" + time);
    }

    /**
     * 将String任意时间转换成某一天LocalDateTime格式
     *
     * @param time string
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(LocalDate date, String time) {
        return LocalDateTime.parse(dateToStr(date, DATE_FORMAT) + "T" + time);
    }


    /**
     * 将time时间转换成毫秒时间戳
     *
     * @param time string
     * @return
     */
    public static long stringDateToMilli(String time) {
        return LocalDateUtils.stringToDate(time, DATE_TIME_FORMAT).toInstant().toEpochMilli();
    }


    /**
     * 将time时间转换成毫秒时间戳
     *
     * @param time LocalDateTime
     * @return
     */
    public static long localDateTimeToMilli(LocalDateTime time) {
        return LocalDateUtils.stringToDate(dateToStr(time, DATE_TIME_FORMAT), DATE_TIME_FORMAT).toInstant().toEpochMilli();
    }

    /**
     * 将毫秒时间戳转换成Date
     *
     * @param time string
     * @return
     */
    public static Date timeMilliToDate(String time) {
        return Date.from(Instant.ofEpochMilli(Long.parseLong(time)));
    }


}