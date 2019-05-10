package project.ys.glass_system.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalDateUtils {
    //默认使用系统当前时区
    public static final ZoneId ZONE = ZoneId.systemDefault();

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_FORMAT_CN = "MM月dd日 ";

    public static final String DATE_FORMAT_MONTH = "MM月 ";

    public static final String DATE_FORMAT_HOUR = "dd日HH时 ";

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

    public static String dateToStr(long time, String format) {
        return dateToStr(dateToLocalDateTime(new Date(time)), format);
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

    public static long strToLong(String time) {
        return LocalDateUtils.stringToDate(time, DATE_FORMAT).toInstant().toEpochMilli();
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



    /*
    * 获取日期集合
    * */
    public static LocalDate getLocalDateByStr(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(str, formatter);
    }

    /**
     * 获取指定日期范围内的所有指定星期 包含开始日期和结束日期
     * @param weeks 1,3,7表示周一，周三，周日
     * @return List<LocalDate>
     */
    public static List<LocalDate> getWeekLocalDateListByRange(LocalDate startDay, LocalDate endDay, List<String> weeks) {
        List<LocalDate> localDateList = new ArrayList<>();
        TemporalField fieldISO = WeekFields.of(DayOfWeek.of(1), 1).dayOfWeek();
        LocalDate tempDay;
        for (String week : weeks) {
            tempDay = startDay.with(fieldISO, Long.parseLong(week));
            if (tempDay.isBefore(startDay)) {
                tempDay = tempDay.plusWeeks(1);
            }
            while (tempDay.isBefore(endDay) || tempDay.isEqual(endDay)) {
                localDateList.add(tempDay);
                tempDay = tempDay.plusWeeks(1);
            }
        }
        return localDateList;
    }

    /**
     * 获取指定日期范围内的所有指定dayOfMonth 包含开始日期和结束日期
     * @param months 1,29,31表示每月的1号，29号，31号
     * @return List<LocalDate>
     */
    public static List<LocalDate> getLocalDateListByMonth(LocalDate startDate,LocalDate endDate,List<String> months){
        List<LocalDate> localDateList = new ArrayList<>();

        LocalDate localDate;
        for(String month : months){
            LocalDate tempDate = startDate;
            while (tempDate.isBefore(endDate) || tempDate.getMonthValue() == endDate.getMonthValue()){
                if(tempDate.lengthOfMonth() >= Integer.valueOf(month)){
                    localDate = tempDate.withDayOfMonth(Integer.valueOf(month));
                    if(localDate.isAfter(startDate) || localDate.isEqual(startDate)){
                        localDate = tempDate.withDayOfMonth(Integer.valueOf(month));
                        if(localDate.isEqual(endDate) || localDate.isBefore(endDate)){
                            localDateList.add(localDate);
                        }
                    }
                }
                tempDate = tempDate.plusMonths(1);
            }
        }
        return localDateList;
    }

    /**
     * 获取指定范围内所有日期，包含开始日期和结束日期
     * @return List<LocalDate>
     */
    public static List<LocalDate> getLocalDateByDay(LocalDate startDate, LocalDate endDate){
        List<LocalDate> localDateList = Stream.iterate(startDate, date -> date.plusDays(1)).
                limit(ChronoUnit.DAYS.between(startDate, endDate))
                .collect(Collectors.toList());
        localDateList.add(endDate);
        return localDateList;
    }
}