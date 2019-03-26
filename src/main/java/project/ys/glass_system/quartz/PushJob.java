package project.ys.glass_system.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import project.ys.glass_system.service.impl.PushServiceImpl;
import project.ys.glass_system.service.impl.SetServiceImpl;
import project.ys.glass_system.util.ApplicationContextUtils;

import java.time.LocalDate;

public class PushJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        PushServiceImpl pushService = ApplicationContextUtils.getBean(PushServiceImpl.class);
        SetServiceImpl setService = ApplicationContextUtils.getBean(SetServiceImpl.class);
        LocalDate date = LocalDate.now();
        pushService.pushEveryUser(date);
    }
}
