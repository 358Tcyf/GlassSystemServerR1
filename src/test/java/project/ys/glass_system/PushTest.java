package project.ys.glass_system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.PushSet;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.model.s.dao.ProductDao;
import project.ys.glass_system.service.impl.*;

import javax.annotation.Resource;
import java.time.LocalDate;

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

    @Resource
    ProductDao productDao;
    @Resource
    UserDao userDao;

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
    public void testPushEveryBody() {
//        LocalDateTime time = LocalDateTime.now();
//        pushService.pushEveryUser(time.toLocalDate(), true);

//        String[] dataArray = new String[]{"2019-03-20"};
//        for (CharSequence data : dataArray) {
//            LocalDate date = LocalDate.parse(data);
//        }
    }

    @Test
    public void testGetui() {
//        String[] dataArray = new String[]{"2019-04-14","2019-04-14",};
//        for (CharSequence data : dataArray) {
//            LocalDate date = LocalDate.parse(data);
//            User p0001 = userDao.findByNo("P0001");
//            pushService.pushWithAlias(date, p0001, true);
//        }
    }

    @Test
    public void testAlarm() {
//        List<AlarmTag> tags = new ArrayList<>();
////        tags.add(new AlarmTag(ALARM_TAGS[0]));
//        tags.add(new AlarmTag(ALARM_TAGS[1]));
//        tags.add(new AlarmTag(ALARM_TAGS[2]));
//        tags.add(new AlarmTag(ALARM_TAGS[3]));
//        System.out.println(pushService.packDailyAlarm(LocalDateTime.now(), tags));
    }

    @After
    public void after() {
        System.out.println("\n=============================");
    }
}
