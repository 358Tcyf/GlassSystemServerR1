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
        if (!userService.isExisted(account)) {
            return RetResponse.makeErrRsp("账号错误");
        } else if (!userService.checkPassword(account, password)) {
            return RetResponse.makeErrRsp("密码错误");
        } else {
            return RetResponse.makeOKRsp("登陆成功", userService.login(account, password));
        }
    }

    @RequestMapping(value = USER + "/userInfo")
    public RetResult<Map<String, Object>> getUserInfo(String account) {
        return RetResponse.makeOKRsp(userService.userInfo(account));
    }

    @RequestMapping(value = USER + "/userList")
    public RetResult<Map<String, Object>> getUserList() {
        return RetResponse.makeOKRsp(userService.userList());
    }

    @RequestMapping(value = USER + "/searchUserList")
    public RetResult<Map<String, Object>> searchUserList(String searchText) {
        return RetResponse.makeOKRsp(userService.searchUserList(searchText));
    }

    @RequestMapping(value = USER + "/addUser")
    public RetResult addUser(String name, String no, String email, String phone, int roleId) {
        userService.addUser(new User(no, "123456", name, phone, email), roleId);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = USER + "/resetPassword")
    public RetResult resetPassword(String account) {
        switch (userService.resetPassword(account)) {
            case 0:
                return RetResponse.makeOKRsp("密码过去已重置");
            case 1:
                return RetResponse.makeOKRsp("密码已重置");
            default:
                return RetResponse.makeErrRsp("账号不存在");
        }
    }


    @RequestMapping(value = USER + "/updateUser")
    public RetResult updateUser(String account, String email, String phone) {
        if (userService.updateUser(account, email, phone))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("账号不存在");
    }

    @RequestMapping(value = USER + "/updatePassword")
    public RetResult updatePassword(String account, String oldPassword, String newPassword) {
        if (!userService.isExisted(account)) {
            return RetResponse.makeErrRsp("账号不存在");
        } else if (!userService.checkPassword(account, oldPassword)) {
            return RetResponse.makeErrRsp("原密码错误");
        } else if (oldPassword.equals(newPassword)) {
            return RetResponse.makeErrRsp("密码一致");
        } else {
            userService.updatePassword(account, newPassword);
            return RetResponse.makeOKRsp("修改成功");
        }
    }


    @RequestMapping(value = USER + "/deleteUser")
    public RetResult deleteUser(String account) {
        userService.logoffUser(account);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = USER + "/latestNo")
    public RetResult latestNo(int roleId) {
        String no = userService.getLatestNo(roleId);
        return RetResponse.makeOKRsp(no);
    }

}
