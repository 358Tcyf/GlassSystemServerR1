package project.ys.glass_system.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Daylight
 * @date 2018/11/28 17:10
 */
public class DateUtils {
    public final static String format1 = "yyyy-MM-dd HH:mm:ss";
    public final static String format2 = "yyyy-MM-dd";


    public static String dateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date strToDate(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        ParsePosition pst = new ParsePosition(0);
        return sdf.parse(str, pst);
    }

    public static Date accurateToDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format1);
        String dateStr = sdf.format(date);
        sdf = new SimpleDateFormat(format2);
        ParsePosition pst = new ParsePosition(0);
        return sdf.parse(dateStr, pst);
    }

}
