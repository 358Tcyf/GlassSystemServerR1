package project.ys.glass_system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.impl.GlassServiceImpl;
import project.ys.glass_system.service.impl.ProductServiceImpl;
import project.ys.glass_system.service.impl.PushServiceImpl;
import project.ys.glass_system.service.impl.SaleServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static project.ys.glass_system.getui.GetuiUtil.*;

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
        LocalDate date = LocalDate.parse("2019-03-09");
        System.out.println(pushService.packDailyData(date));
    }

    @Test
    public void testGetui() {
        LocalDate date = LocalDate.parse("2019-03-09");
        sendMessage(transmissionTemplate(pushService.packDailyData(date).toString()));
    }

    @After
    public void after() {
        System.out.println("\n=============================");
    }
}
