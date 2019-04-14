package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ys.glass_system.config.Unlimited;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.Alarm;
import project.ys.glass_system.model.p.entity.Push;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.impl.PushServiceImpl;
import project.ys.glass_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static project.ys.glass_system.constant.HttpConstant.*;

@Unlimited
@RestController
@RequestMapping(PUSH)
public class PushController {

    @Resource
    PushServiceImpl pushService;

    @Resource
    UserServiceImpl userService;

    @Resource
    UserDao userDao;


    @RequestMapping(INSTANT)
    public RetResult instantPush() {
        pushService.pushEveryUser(LocalDate.now(), true);
        return RetResponse.makeOKRsp("发送成功");
    }

    @RequestMapping(INSTANT + "/{alias:.+}")
    public RetResult instantPush(@PathVariable String alias) {
        User user = userDao.findByNo(alias);
        if (user != null) {
            pushService.pushWithAlias(LocalDate.now(), user, true);
            return RetResponse.makeOKRsp("发送成功");
        } else
            return RetResponse.makeErrRsp("找不到用户");
    }

    @RequestMapping(UPDATE_PUSH)
    public RetResult updatePush(String account, @RequestBody List<Push> pushes) {
        if (userService.isExisted(account)) {
            pushService.updatePush(account, pushes);
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("账号不存在");
        }
    }

    @RequestMapping(DOWNLOAD_PUSH)
    public RetResult downloadPush(String account) {
        if (userService.isExisted(account)) {
            return RetResponse.makeOKRsp(pushService.downloadPush(account));
        } else {
            return RetResponse.makeErrRsp("账号不存在");
        }
    }


    @RequestMapping(UPDATE_ALARM)
    public RetResult updateAlarm(String account, @RequestBody List<Alarm> alarms) {
        if (userService.isExisted(account)) {
            pushService.updateAlarm(account, alarms);
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("账号不存在");
        }
    }

    @RequestMapping(DOWNLOAD_ALARM)
    public RetResult downloadAlarm(String account) {
        if (userService.isExisted(account)) {
            return RetResponse.makeOKRsp(pushService.downloadAlarm(account));
        } else {
            return RetResponse.makeErrRsp("账号不存在");
        }
    }
}
