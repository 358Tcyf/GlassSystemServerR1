package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class UserController {

    private static final String USER = "/user";

    @Resource
    UserServiceImpl userService;

    @RequestMapping(value = USER + "/login")
    public RetResult<User> login(String account, String password) {
        System.out.println("account is " + account + "\npassword is " + password);
        if (!userService.isExisted(account)) {
            return RetResponse.makeErrRsp("账号错误");
        } else if (!userService.checkPassword(account, password)) {
            return RetResponse.makeErrRsp("密码错误");
        } else {
            System.out.println(account + "login success!");
            return RetResponse.makeOKRsp("登陆成功", userService.login(account, password));
        }
    }

    @RequestMapping(value = USER + "/userInfo")
    public RetResult<Map<String, Object>> getUserInfo(String account) {
        System.out.println(account + "GET SUCCESS!");
        return RetResponse.makeOKRsp(userService.userInfo(account));
    }

    @RequestMapping(value = USER + "/userList")
    public RetResult<Map<String, Object>> getUserList() {
        return RetResponse.makeOKRsp(userService.userList());
    }


    @RequestMapping(value = USER + "/addUser")
    public RetResult addUser(String name, String no, String email, String phone, int roleId) {
        userService.addUser(new User(no, "123456", name, phone, email), roleId);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = USER + "/resetPassword")
    public RetResult resetPassword(String account) {
        userService.resetPassword(account);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = USER + "/deleteUser")
    public RetResult deleteUser(String account) {
        userService.logoffUser(account);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = USER + "/latestNo")
    public RetResult latestNo(int roleId) {
        String no = userService.getLatestNo(roleId);
        System.out.println(no);
        return RetResponse.makeOKRsp(no);
    }

}
