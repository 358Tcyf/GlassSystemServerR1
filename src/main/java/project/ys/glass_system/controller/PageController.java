package project.ys.glass_system.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.util.SessionUtil;

import static project.ys.glass_system.constant.HttpConstant.*;


@Controller
public class PageController {

    @GetMapping(LOGIN)
    public String login() {
        return USER + LOGIN;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", SessionUtil.getInstance().getUser());
        return COMMON + INDEX;
    }

    @GetMapping(HOME)
    public String home(Model model) {
        User loginUser = SessionUtil.getInstance().getUser();
        return USER + USER_MANAGER;
    }

    @GetMapping(USER + USER_MANAGER)
    public String userManager(Model model) {
        return USER + USER_MANAGER;
    }


    @GetMapping(USER + RESET_PASSWORD)
    public String changePwd() {
        return USER + RESET_PASSWORD;
    }

    @GetMapping(REDIRECT)
    public String redirect() {
        return COMMON + REDIRECT;
    }

    @GetMapping(_401)
    public String unauthorized() {
        return COMMON + _401;
    }

    @GetMapping(_404)
    public String pageNotFound() {
        return COMMON + _404;
    }

    @GetMapping(ERROR)
    public String error() {
        return COMMON + ERROR;
    }

}
