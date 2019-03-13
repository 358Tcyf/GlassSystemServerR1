package project.ys.glass_system.quartz;

import com.alibaba.fastjson.JSON;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import project.ys.glass_system.service.impl.GlassServiceImpl;
import project.ys.glass_system.service.impl.PushServiceImpl;
import project.ys.glass_system.service.impl.SaleServiceImpl;
import project.ys.glass_system.util.ApplicationContextUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static project.ys.glass_system.getui.GetuiUtil.sendMessage;
import static project.ys.glass_system.getui.GetuiUtil.transmissionTemplate;

public class PushJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        PushServiceImpl pushService = ApplicationContextUtils.getBean(PushServiceImpl.class);

        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        System.out.println("系统于" + LocalDateTime.now() + "发布了推送");

        LocalDateTime time = LocalDateTime.now();
        String push = JSON.toJSONString(pushService.packDailyData(time.toLocalDate()));
        sendMessage(transmissionTemplate(push));

    }
}
