package project.ys.glass_system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.impl.GlassServiceImpl;
import project.ys.glass_system.service.impl.ProductServiceImpl;
import project.ys.glass_system.service.impl.RecordServiceImpl;
import project.ys.glass_system.service.impl.SaleServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GlassSystemApplication.class)
public class RecordTest {

    @Resource
    RecordServiceImpl recordService;

    @Resource
    UserDao userDao;


    @Before
    public void before() {
        System.out.println("=============================\n");
    }

    @Test
    public void testAdd() {
//        String account = "A01";
//        User user = userDao.findDistinctByNoOrPhoneOrEmail(account,account,account);
//        System.out.println(user.getNo());
    }

    @After
    public void after() {
        System.out.println("\n=============================");
    }
}
