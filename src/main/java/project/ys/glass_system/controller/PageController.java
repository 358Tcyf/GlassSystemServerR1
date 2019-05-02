package project.ys.glass_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.ys.glass_system.config.SessionUtil;
import project.ys.glass_system.config.Unlimited;
import project.ys.glass_system.model.p.entity.User;

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
        User user = SessionUtil.getInstance().getUser();
        model.addAttribute("user", user);
        if (user.getNo().startsWith("A"))
            return _HOME + HOME_ROOT;
        else
            return _PUSH + PUSH_MANAGER_SELF;
    }

    @GetMapping(USER + USER_MANAGER)
    public String userManager() {
        return _USER + USER_MANAGER;
    }

    @GetMapping(PUSH + PUSH_MANAGER)
    public String pushManager() {
        return _PUSH + PUSH_MANAGER;
    }

    @Unlimited
    @GetMapping(PUSH_CHART_TABS + "/{uuid:.+}")
    public String pushChartTabs(@PathVariable String uuid, Model model) {
        model.addAttribute("pushId", uuid);
        return _PUSH + PUSH_CHART_TABS;
    }

    @Unlimited
    @GetMapping(PUSH_CHART + "/{uuid:.+}")
    public String pushChart(@PathVariable String uuid, Model model) {
        model.addAttribute("pushId", uuid);
        return _PUSH + PUSH_CHART;
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
