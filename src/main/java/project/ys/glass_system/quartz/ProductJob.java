package project.ys.glass_system.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.impl.GlassServiceImpl;
import project.ys.glass_system.service.impl.ProductServiceImpl;
import project.ys.glass_system.util.ApplicationContextUtils;
import project.ys.glass_system.util.RandomUtils;

import java.time.LocalDateTime;

import static project.ys.glass_system.model.s.entity.Glass.GLASS_MODEL;
import static project.ys.glass_system.util.RandomUtils.randomInt;

public class ProductJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ProductServiceImpl productService = ApplicationContextUtils.getBean(ProductServiceImpl.class);
        GlassServiceImpl glassService = ApplicationContextUtils.getBean(GlassServiceImpl.class);

//        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        System.out.println("某员工于" + LocalDateTime.now() + "该时进行了生产");
        int random = RandomUtils.randomInt(0, 4);
        Products onceProduce = glassService.virtualProduce(GLASS_MODEL[random]);
        productService.onceProduct(onceProduce);
    }
}
