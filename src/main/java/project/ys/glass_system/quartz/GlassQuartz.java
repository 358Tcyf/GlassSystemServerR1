package project.ys.glass_system.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static project.ys.glass_system.util.LocalDateUtils.*;

public class GlassQuartz {

    public static void onceProductNew() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try {
            scheduler = sf.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        JobDetail jb = JobBuilder.newJob(ProductNewJob.class)
                .withDescription("Daily ProduceJob") //job的描述
                .build();


        //向任务传递数据
        JobDataMap jobDataMap = jb.getJobDataMap();
//        jobDataMap.put("date", LocalDateTime.now());


        LocalTime time = LocalTime.now();
        int hour = time.getHour();
        if (hour == 23)
            hour = 0;
        else
            hour = hour + 1;
        String h = String.valueOf(hour);
        if (h.length() == 1)
            h = "0" + h;
        int min = time.getMinute();
        String m;
        if (min < 30)
            m = "30";
        else
            m = "00";
        LocalDateTime dateTime = LocalDateTime.parse(dateToStr(LocalDate.now(), DATE_FORMAT) + "T" + h + ":" + m + ":00");
        Date statTime = localDateTimeToDate(dateTime);
//        LocalDateTime dateTime = LocalDateTime.parse(dateToStr(LocalDate.now(), DATE_FORMAT) + "T00:01:00");

        //4.创建Trigger
        //使用SimpleScheduleBuilder或者CronScheduleBuilder
        Trigger t = TriggerBuilder.newTrigger()
                .withDescription("")
                .startAt(statTime)  //默认当前时间启动
                //普通计时器
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever())//间隔30min，永远执行
                //表达式计时器
//                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")) //10秒执行一次
                .build();
        try {
            scheduler.scheduleJob(jb, t);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static void oncePushNew() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try {
            scheduler = sf.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        JobDetail jb = JobBuilder.newJob(PushNewJob.class)
                .withDescription("Daily ProduceJob") //job的描述
                .build();


        //向任务传递数据
        JobDataMap jobDataMap = jb.getJobDataMap();
//        jobDataMap.put("date", LocalDateTime.now());
        LocalTime time = LocalTime.now();
        int hour = time.getHour();
        if (hour == 23)
            hour = 0;
        else
            hour = hour + 1;
        String h = String.format("%02d", hour);
        LocalDateTime dateTime = LocalDateTime.parse(dateToStr(LocalDate.now(), DATE_FORMAT) + "T" + h + ":00:00");
        Date statTime = localDateTimeToDate(dateTime);

        //4.创建Trigger
        //使用SimpleScheduleBuilder或者CronScheduleBuilder
        Trigger t = TriggerBuilder.newTrigger()
                .withDescription("")
                .startAt(statTime)  //默认当前时间启动
                //普通计时器
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(60).repeatForever())//间隔60min，永远执行
                .build();
        try {
            scheduler.scheduleJob(jb, t);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}

