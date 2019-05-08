package project.ys.glass_system.constant;

import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjuster;

public class AutoPushConstant {

    public static String DailyProduce = "日生产量";
    public static String DailyTestRank = "日质检品级";
    public static String DailyConsume = "日生产能耗";

    public static String WeeklyProduce = "周生产量";
    public static String WeeklyTestRank = "周质检品级";
    public static String WeeklyConsume = "周生产能耗";

    public static String MonthlyProduce = "月生产量";
    public static String MonthlyTestRank = "月质检品级";
    public static String MonthlyConsume = "月生产能耗";

    public static final String[] PUSH_TABS = {DailyProduce, DailyConsume, DailyTestRank
    ,WeeklyProduce,WeeklyTestRank,WeeklyConsume};
    public static final String[] ConsumeLabel = {"水消耗", "电消耗", "原料消耗", "煤消耗"};

    public static final String[] Weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    public static final TemporalAdjuster[] Adjuster = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,DayOfWeek.FRIDAY,DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};

}
