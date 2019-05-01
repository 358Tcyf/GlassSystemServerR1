package project.ys.glass_system.constant;

public class PushConstant {
    public static String DailyProduceCountList = "生产量";
    public static String DailyCountOfModel = "生产型号统计";
    public static String DailyProduceQualityList = "生产质量";
    public static String DailyConsume = "生产能耗";
    public static String DailySaleCount = "销售量";
    public static String DailyDeliveryCount = "交易量";
    public static String DailySale = "销售额";
    public static String DailyCustomRate = "顾客满意率";

    public static String[] PUSH_CHARTS = {DailyProduceCountList,DailyCountOfModel,DailyProduceQualityList,DailyConsume,DailySaleCount,DailyDeliveryCount,DailySale,DailyCustomRate};
    public static final String[] PUSH_TAGS = PUSH_CHARTS;
    public static final String[] PRODUCE_TAGS = {DailyProduceCountList,DailyCountOfModel,DailyProduceQualityList,DailyConsume,};
    public static final String[] SALES_TAGS = {DailySaleCount,DailyDeliveryCount,DailySale,DailyCustomRate};;
    public static final String[] PUSH_TIME = {"每4小时一次", "每8小时一次", "每12小时一次", "每天一次"};
}