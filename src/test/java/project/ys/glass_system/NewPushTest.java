package project.ys.glass_system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.t.dao.GlassModelDao;
import project.ys.glass_system.service.t.impl.AutoPushServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GlassSystemApplication.class)
public class NewPushTest {
    @Resource
    GlassModelDao modelDao;

    @Resource
    AutoPushServiceImpl pushService;

    //    @Test
    public void testGetLabels() {

    }
    @Test
    public void testPush() {
        pushService.pushEveryOne(LocalDate.now(), true);
    }

    //    @Test
    public void testCreateChart() {
//        System.out.println(pushService.dailyProduceChart(LocalDate.now()));
//        System.out.println(pushService.dailyTestChart(LocalDate.now()));
//        System.out.println(pushService.dailyConsumeChart(LocalDate.now()));
//        System.out.println(pushService.weeklyProduceChart(LocalDate.now()));
//        System.out.println(pushService.weeklyTestChart(LocalDate.now()));
    }

    @Test
    public void testOthers() {
    }
}
