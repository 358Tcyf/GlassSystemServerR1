package project.ys.glass_system;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.s.entity.Glass;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.impl.GlassServiceImpl;
import project.ys.glass_system.service.impl.ProductServiceImpl;
import project.ys.glass_system.service.impl.PushServiceImpl;
import project.ys.glass_system.service.impl.SaleServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static project.ys.glass_system.getui.GetuiUtil.*;
import static project.ys.glass_system.util.LocalDateUtils.DATE_FORMAT_CN;
import static project.ys.glass_system.util.LocalDateUtils.dateToStr;

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

    @Before
    public void before() {
        System.out.println("=============================\n");
    }


    @Test
    public void testPushItem1() {
//        LocalDate date = LocalDate.parse("2019-03-09");
//        System.out.println(pushService.packDailyData(date));
    }

    @Test
    public void testGetui() {
//        String[] dataArray = new String[]{"2019-03-09", "2019-03-10"};
//        for (CharSequence data : dataArray) {
//            LocalDate date = LocalDate.parse(data);
//            String push = JSON.toJSONString(pushService.packDailyData(date));
//            sendMessage(transmissionTemplate(push));
//        }
    }

    @After
    public void after() {
        System.out.println("\n=============================");
    }
}
