package project.ys.glass_system.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class ProductQuartz {

    public static void onceProduct() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try {
            scheduler = sf.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        JobDetail jb = JobBuilder.newJob(ProductJob.class)
                .withDescription("daily produce") //job的描述
                .build();


        //向任务传递数据
        JobDataMap jobDataMap = jb.getJobDataMap();
        jobDataMap.put("date", new Date(System.currentTimeMillis()));
        long time = System.currentTimeMillis() + 10 * 1000L; //10秒后启动任务
        Date statTime = new Date(time);

        //4.创建Trigger
        //使用SimpleScheduleBuilder或者CronScheduleBuilder
        Trigger t = TriggerBuilder.newTrigger()
                .withDescription("")
                .startAt(statTime)  //默认当前时间启动
                //普通计时器
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever())//间隔10秒，永远执行
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

}
