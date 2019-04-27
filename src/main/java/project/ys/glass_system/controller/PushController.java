package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.*;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static project.ys.glass_system.constant.HttpConstant.*;
import static project.ys.glass_system.util.DateUtils.format1;
import static project.ys.glass_system.util.LocalDateUtils.dateToStr;

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
            System.out.println(dateToStr(LocalDateTime.now(), format1) + "请求忽略日期和时间限制");
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

    @RequestMapping(PUSH_QUERY)
    @ResponseBody
    public RetResult<Map<String, Object>> pushQuery(String title, long startTime, long endTime, String receiverID, String receiver, int type, int read, int page, int limit) {
        return RetResponse.makeOKRsp(pushService.pushQuery(title, startTime, endTime, receiverID, receiver, type, read, page, limit));
    }

    @RequestMapping(PUSH_DELETE)
    public RetResult deletePush(String uuid) {
        if (pushService.deletePush(uuid))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("没有这条推送了");
    }

    @RequestMapping(PUSH_LIST_DELETE)
    public RetResult deletePushList(List<String> uuids) {
        pushService.deletePushList(uuids);
        return RetResponse.makeOKRsp();
    }
}
