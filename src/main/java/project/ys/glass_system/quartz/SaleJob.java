package project.ys.glass_system.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import project.ys.glass_system.model.s.entity.Customers;
import project.ys.glass_system.model.s.entity.OrderItems;
import project.ys.glass_system.model.s.entity.Orders;
import project.ys.glass_system.service.impl.GlassServiceImpl;
import project.ys.glass_system.service.impl.SaleServiceImpl;
import project.ys.glass_system.util.ApplicationContextUtils;
import project.ys.glass_system.util.RandomUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static project.ys.glass_system.model.s.entity.Customers.CUST_PHONE;
import static project.ys.glass_system.model.s.entity.Glass.GLASS_MODEL;
import static project.ys.glass_system.util.RandomUtils.randomInt;

public class SaleJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        GlassServiceImpl glassService = ApplicationContextUtils.getBean(GlassServiceImpl.class);
        SaleServiceImpl saleService = ApplicationContextUtils.getBean(SaleServiceImpl.class);

//        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        System.out.println("某员工于" + LocalDateTime.now() + "该时完成了销售任务");
        Customers customer = glassService.getCustomer(CUST_PHONE[RandomUtils.randomInt(0, 5)]);
        List<OrderItems> orderItems = new ArrayList<>();
        for (int i = 0; i < RandomUtils.randomInt(1, 3); i++) {
            orderItems.add(glassService.virtualSale(GLASS_MODEL[RandomUtils.randomInt(0, 4)]));
        }
        Orders orders = new Orders(LocalDateTime.now(), customer);
        orders.setRate(RandomUtils.randomInt(4.0, 5.0));
        saleService.ordersOfDay(saleService.ordersOfCustomer(orders, orderItems));
    }
}
