package project.ys.glass_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.ys.glass_system.config.SessionUtil;
import project.ys.glass_system.config.Unlimited;
import static project.ys.glass_system.constant.HttpConstant.*;

@Controller
public class PageController {

    @Unlimited
    @GetMapping(LOGIN)
    public String login() {
        return _USER + LOGIN;
    }

    @Unlimited
    @GetMapping(ABOUT)
    public String index() {
        return _ABOUT + INDEX;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", SessionUtil.getInstance().getUser());
        return _COMMON + INDEX;
    }

    @GetMapping(HOME)
    public String home(Model model) {
        return _USER + USER_MANAGER;
    }

    @GetMapping(USER + USER_MANAGER)
    public String userManager(Model model) {
        return _USER + USER_MANAGER;
    }

    @GetMapping(PUSH + PUSH_MANAGER)
    public String pushManager(Model model) {
        return _PUSH + PUSH_MANAGER;
    }

    @GetMapping(PUSH + ALARM_MANAGER)
    public String alarmManager(Model model) {
        return _PUSH + ALARM_MANAGER;
    }

    @Unlimited
    @GetMapping(REDIRECT)
    public String redirect() {
        return _COMMON + REDIRECT;
    }

    @Unlimited
    @GetMapping(_401)
    public String unauthorized() {
        return _COMMON + _401;
    }

    @Unlimited
    @GetMapping(_404)
    public String pageNotFound() {
        return _COMMON + _404;
    }

    @GetMapping(ERROR)
    @Unlimited
    public String error() {
        return _COMMON + ERROR;
    }
}
