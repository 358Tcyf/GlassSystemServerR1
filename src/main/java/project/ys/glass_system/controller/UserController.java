package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static project.ys.glass_system.constant.HttpConstant.*;
import static project.ys.glass_system.util.RegexUtils.checkEmail;
import static project.ys.glass_system.util.RegexUtils.checkMobile;


@RestController
@RequestMapping(USER)
public class UserController {


    @Resource
    UserServiceImpl userService;

    @RequestMapping(LOGIN)
    public RetResult<User> login(String account, String password) {
        if (account.length() > 5 || account.length() < 3) {
            if (!checkEmail(account)) {
                if (!checkMobile(account)) {
                    System.out.println("不是合法的账号");
                    return RetResponse.makeErrRsp("不是合法的账号");
                }
            }
        }
        if (!userService.isExisted(account)) {
            System.out.println("账号错误");
            return RetResponse.makeErrRsp("账号错误");
        } else if (!userService.checkPassword(account, password)) {
            System.out.println("密码错误");
            return RetResponse.makeErrRsp("密码错误");
        } else {
            System.out.println("登陆成功");
            return RetResponse.makeOKRsp("登陆成功", userService.login(account, password));
        }
    }

    @GetMapping(LOGOUT)
    public RetResult logout(HttpServletRequest request) {
//        SessionUtil.getInstance().logout();
        request.getSession().invalidate();
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(USER_INFO)
    public RetResult<Map<String, Object>> getUserInfo(String account) {
        return RetResponse.makeOKRsp(userService.userInfo(account));
    }

    @RequestMapping(USER_LIST)
    public RetResult<Map<String, Object>> getUserList() {
        return RetResponse.makeOKRsp(userService.userList());
    }


    @RequestMapping(USER_QUERY)
    @ResponseBody
    public RetResult<Map<String, Object>> userQuery(String name, String account, int role, String phone, String email, int page, int limit) {
        return RetResponse.makeOKRsp(userService.userQuery(name, account, role, phone, email, page, limit));
    }

    @RequestMapping(SEARCH_USER)
    public RetResult<Map<String, Object>> searchUserList(String searchText) {
        return RetResponse.makeOKRsp(userService.searchUserList(searchText));
    }

    @RequestMapping(ADD_USER)
    @ResponseBody
    public RetResult addUser(String name, String no, String email, String phone, Integer roleId) {
        if (roleId == 1 || roleId == 2 || roleId == 3) {
            userService.addUser(new User(no, "123456", name, phone, email), roleId);
            return RetResponse.makeOKRsp();
        } else
            return RetResponse.makeErrRsp("创建用户失败");

    }

    @RequestMapping(RESET_PASSWORD)
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

    @RequestMapping(UPDATE_USER)
    @ResponseBody
    public RetResult updateUser(String account, String email, String phone) {
        if (userService.updateUser(account, email, phone))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("账号不存在");
    }

    @RequestMapping(UPDATE_PASSWORD)
    @ResponseBody
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


    @RequestMapping(USER_DELETE)
    public RetResult deleteUser(String account) {
        userService.logoffUser(account);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(LATEST_NO)
    public RetResult latestNo(int roleId) {
        String no = userService.getLatestNo(roleId);
        return RetResponse.makeOKRsp(no);
    }


}
