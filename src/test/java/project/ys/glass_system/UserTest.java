package project.ys.glass_system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GlassSystemApplication.class)
public class UserTest {

    public static final int SUPER_MANAGER = 0;
    public static final int MANAGEMENT_SECTION = 1;
    public static final int PRODUCT_SECTION = 2;
    public static final int SALE_SECTION = 3;

    @Resource
    UserServiceImpl userService;


    @Test
    public void testAddUser() {
//        userService.addUser(new User("M0002", "123456", "布鲁斯", "", ""), MANAGEMENT_SECTION);
    }

    @Test
    public void testCreateRole() {
//        userService.create(new Role("超级管理员"));
//        userService.create(new Role("管理部门"));
//        userService.create(new Role("生产部门"));
//        userService.create(new Role("销售部门"));
    }

    @Test
    public void testGetLatestNo() {
//        userService.getLatestNo(SUPER_MANAGER);
    }
}
