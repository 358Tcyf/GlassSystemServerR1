package project.ys.glass_system.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import project.ys.glass_system.service.t.impl.AutoPushServiceImpl;
import project.ys.glass_system.util.ApplicationContextUtils;

import java.time.LocalDate;

public class PushNewJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AutoPushServiceImpl pushService = ApplicationContextUtils.getBean(AutoPushServiceImpl.class);
        LocalDate date = LocalDate.now();
        pushService.pushEveryOne(date, false);
    }
}
