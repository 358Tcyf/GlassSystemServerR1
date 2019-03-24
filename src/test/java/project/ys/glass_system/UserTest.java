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

    public static final int SUPER_MANAGER = 1;
    public static final int MANAGEMENT_SECTION = 2;
    public static final int PRODUCT_SECTION = 3;
    public static final int SALE_SECTION = 4;
    public static final String DEFAULT_PASSWORD = "123456";

    @Resource
    UserServiceImpl userService;


    @Test
    public void testAddUser() {
//       for(int i =0;i<10;i++){
//           int roleId = new Random().nextInt(4) + 1;
//           String no = userService.getLatestNo(roleId);
//           System.out.println(no);
//       }
    }

    @Test
    public void createUser() {
//        for (int i = 0; i < 20; i++) {
//            int roleId = new Random().nextInt(4) + 1;
//            String no = userService.getLatestNo(roleId);
//            String password = DEFAULT_PASSWORD;
//            String name = getChineseName();
//            String phone = getTelephone();
//            String email = getEmail(5, 10);
//            User user;
//            if (roleId == 1)
//                user = new User(no, password);
//            else
//                user = new User(no, password, name, phone, email);
//            userService.addUser(user, roleId);
//        }
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

    @Test
    public void testGetUserInfo() {
        System.out.println(userService.userInfo("P0001"));
//        userService.getLatestNo(SUPER_MANAGER);
    }
}
