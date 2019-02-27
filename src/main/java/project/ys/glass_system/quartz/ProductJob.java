package project.ys.glass_system.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import project.ys.glass_system.GlassSystemApplication;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.impl.ProductServiceImpl;
import project.ys.glass_system.utils.ApplicationContextUtil;

import javax.annotation.Resource;
import java.util.Date;

public class ProductJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时器任务执行" + new Date(System.currentTimeMillis()));
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        System.out.println("某员工于" + map.get("date") + "该时进行了生产");
        Products products = new Products((Date) map.get("date"));
        ProductServiceImpl productService = ApplicationContextUtil.getBean(ProductServiceImpl.class);
        productService.onceProduct(products);
    }
}
