package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.model.p.entity.PushSet;
import project.ys.glass_system.service.impl.SetServiceImpl;
import project.ys.glass_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class SetController {

    private static final String SET = "/set";

    @Resource
    UserServiceImpl userService;

    @Resource
    SetServiceImpl setService;

    @RequestMapping(value = SET + "/getSets")
    public RetResult<PushSet> getSets(String account) {
        if (!userService.isExisted(account))
            return RetResponse.makeErrRsp("用户不存在");
        else {
            PushSet set = setService.getPushSet(account);
            return RetResponse.makeOKRsp(set);
        }
    }


    @RequestMapping(value = SET + "/getTags")
    public RetResult getTags(String account) {
        if (!userService.isExisted(account))
            return RetResponse.makeErrRsp("用户不存在");
        else
            return RetResponse.makeOKRsp(setService.getTags(account));
    }

    @RequestMapping(value = SET + "/uploadSet")
    public RetResult uploadSet(String account, @RequestBody PushSet pushSet) {
        if (setService.updateSet(account, pushSet))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("账号不存在");
    }

    @RequestMapping(value = SET + "/updateTags")
    public RetResult updateTags(String account, @RequestBody List<String> tags) {
        if (setService.updateTags(account, tags))
            return RetResponse.makeOKRsp();
        else
            return RetResponse.makeErrRsp("账号不存在");
    }

}
