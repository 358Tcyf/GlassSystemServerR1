package project.ys.glass_system.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import project.ys.glass_system.service.t.impl.AutoProduceServiceImpl;
import project.ys.glass_system.util.ApplicationContextUtils;

import java.time.LocalDate;

public class ProductNewJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AutoProduceServiceImpl productService = ApplicationContextUtils.getBean(AutoProduceServiceImpl.class);
//        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        productService.produceStart(LocalDate.now(), false);
        productService.produce(false);
        productService.store(false);
        productService.testStart(LocalDate.now(), false);
        productService.test(false);
    }
}
