package project.ys.glass_system;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.p.entity.PushSet;
import project.ys.glass_system.service.impl.*;

import javax.annotation.Resource;
import java.time.LocalDate;

import static project.ys.glass_system.getui.GetuiUtil.sendMessage;
import static project.ys.glass_system.getui.GetuiUtil.transmissionTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GlassSystemApplication.class)
public class PushTest {


    @Resource
    ProductServiceImpl productService;

    @Resource
    GlassServiceImpl glassService;

    @Resource
    SaleServiceImpl saleService;

    @Resource
    PushServiceImpl pushService;

    @Resource
    SetServiceImpl setService;

    @Before
    public void before() {
        System.out.println("=============================\n");
    }


    @Test
    public void createSet() {
//        Date date = new Date();
//        date.setHours(0);
//        date.setMinutes(0);
//        date.setSeconds(0);
//        long start = date.getTime();
//        System.out.println(date);
//        System.out.println(start);
//        date.setHours(23);
//        date.setMinutes(59);
//        date.setSeconds(59);
//        long end = date.getTime();
//        System.out.println(date);
//        System.out.println(end);
    }


    @Test
    public void testPushItem1() {
//        LocalDate date = LocalDate.parse("2019-03-09");
//        System.out.println(pushService.packDailyData(date));
    }

    @Test
    public void testGetui() {
//        String[] dataArray = new String[]{"2019-03-20", "2019-03-21", "2019-03-22", "2019-03-23",};
//        for (CharSequence data : dataArray) {
//            LocalDate date = LocalDate.parse(data);
//            PushSet pushSet = setService.getPushSet("P0001");
//            String push = JSON.toJSONString(pushService.packDailyData(date,pushSet.getTags()));
//            sendMessage(transmissionTemplate(push));
//        }
    }

    @After
    public void after() {
        System.out.println("\n=============================");
    }
}
