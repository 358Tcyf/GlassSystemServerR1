package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.impl.PushServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDate;

import static project.ys.glass_system.constant.HttpConstant.INSTANT;
import static project.ys.glass_system.constant.HttpConstant.PUSH;

@RestController
@RequestMapping(PUSH)
public class PushController {

    @Resource
    PushServiceImpl pushService;

    @Resource
    UserDao userDao;


    @RequestMapping(INSTANT)
    public RetResult instantPush() {
        pushService.pushEveryUser(LocalDate.now(), true);
        return RetResponse.makeOKRsp("发送成功");
    }

    @RequestMapping(INSTANT + "/{alias:.+}")
    public RetResult instantPush(String alias) {
        User user = userDao.findByNo(alias);
        if (user != null) {
            pushService.pushWithAlias(LocalDate.now(), user, true);
        }
        return RetResponse.makeOKRsp("发送成功");
    }

}
