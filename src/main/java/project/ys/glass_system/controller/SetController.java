package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ys.glass_system.config.Unlimited;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.model.p.entity.AlarmTag;
import project.ys.glass_system.model.p.entity.PushSet;
import project.ys.glass_system.service.impl.SetServiceImpl;
import project.ys.glass_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;
import java.util.List;

import static project.ys.glass_system.constant.HttpConstant.*;

@Unlimited
@RestController
@RequestMapping(SET)
public class SetController {

    @Resource
    UserServiceImpl userService;

    @Resource
    SetServiceImpl setService;

    @RequestMapping(GET_SETS)
    public RetResult<PushSet> getSets(String account) {
        if (!userService.isExisted(account))
            return RetResponse.makeErrRsp("用户不存在");
        else {
            PushSet set = setService.getPushSet(account);
            return RetResponse.makeOKRsp(set);
        }
    }


    @RequestMapping(GET_TAGS)
    public RetResult getTags(String account) {
        if (!userService.isExisted(account))
            return RetResponse.makeErrRsp("用户不存在");
        else
            return RetResponse.makeOKRsp(setService.getTags(account));
    }

    @RequestMapping(GET_ALARM_TAGS)
    public RetResult getAlarmTags(String account) {
        if (!userService.isExisted(account))
            return RetResponse.makeErrRsp("用户不存在");
        else
            return RetResponse.makeOKRsp(setService.getAlarmTags(account));
    }

    @RequestMapping(CANCEL_SMART_SUB)
    public RetResult cancelSmartSub(String account) {
        if (!userService.isExisted(account))
            return RetResponse.makeErrRsp("用户不存在");
        else
            setService.cancelSmartSub(account);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(UPLOAD_SETS)
    public RetResult uploadSet(String account, @RequestBody PushSet pushSet) {
        if (setService.updateSet(account, pushSet))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("账号不存在");
    }

    @RequestMapping(UPDATE_TAGS)
    public RetResult updateTags(String account, @RequestBody List<String> tags) {
        if (setService.updateTags(account, tags))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("账号不存在");
    }

    @RequestMapping(CANCEL_TAGS)
    public RetResult cancelTags(String account, @RequestBody List<String> tags) {
        if (setService.cancelTags(account, tags))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("账号不存在");
    }

    @RequestMapping(UPDATE_ALARM_TAGS)
    public RetResult updateAlarmTags(String account, @RequestBody List<AlarmTag> tags) {
        if (setService.updateAlarmTags(account, tags))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("账号不存在");
    }




}
