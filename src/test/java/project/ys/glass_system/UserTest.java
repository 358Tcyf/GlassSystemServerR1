package project.ys.glass_system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import project.ys.glass_system.model.p.dao.RoleDao;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.Role;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;
import java.util.List;

import static project.ys.glass_system.util.RandomValueUtils.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GlassSystemApplication.class)
public class UserTest {

    public static final int SUPER_MANAGER = 1;
    public static final int MANAGEMENT_SECTION = 2;
    public static final int PRODUCT_SECTION = 3;
    public static final int SALE_SECTION = 4;
    public static final String DEFAULT_PASSWORD = "123456";

    @Resource
    RoleDao roleDao;

    @Resource
    UserDao userDao;
    @Resource
    UserServiceImpl userService;

//    @Before
    public void before() {
        if (roleDao.findAll().size() <= 0)
            testCreateRole();
        if (userDao.findAll().size() <= 0) {
            testCreateUser();
        }
//        testCreateWareHouse();
//        if (rankDao.findAll().size() <= 0)
//            testCreateRank();
        System.out.println("=============================\n");
    }

    //        @Test
    public void testCreateRole() {
        userService.create(new Role("超级管理员"));
        userService.create(new Role("行政部门"));
        userService.create(new Role("研发部门"));
        userService.create(new Role("采购部门"));
        userService.create(new Role("生产部门"));
        userService.create(new Role("仓储部门"));
        userService.create(new Role("质检部门"));
        userService.create(new Role("销售部门"));
        userService.create(new Role("财务部门"));
    }

    //    @Test
    public void testCreateUser() {
        List<Role> roles = roleDao.findAll();
        for (Role role : roles) {
            for (int i = 0; i < 1; i++) {
                String no = userService.getLatestNo(role.getId());
                String password = DEFAULT_PASSWORD;
                String name = getChineseName();
                String phone = getTelephone();
                String email = getEmail(5, 10);
                User user = new User(no, password, name, phone, email);
                userService.addUser(user, role);
            }
        }
    }


    @Test
    public void testGetLatestNo() {
//        userService.getLatestNo(SUPER_MANAGER);
    }

//    @Test
    public void testGetUserInfo() {
        System.out.println(userService.userQuery("","",0,"","",1,20));
//        System.out.println(userService.userInfo("SA01"));
    }
}
