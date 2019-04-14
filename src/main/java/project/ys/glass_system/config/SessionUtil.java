package project.ys.glass_system.config;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import project.ys.glass_system.model.p.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;


public class SessionUtil {

    private static SessionUtil instance;

    private SessionUtil() {
    }

    public static SessionUtil getInstance() {
        synchronized (SessionUtil.class) {
            if (Objects.isNull(instance))
                instance = new SessionUtil();
        }
        return instance;
    }

    private HttpServletRequest getHttpRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    private HttpSession getHttpSession() {
        return getHttpRequest().getSession();
    }

    public void setSessionMap(User user) {
        //不缓存密码
        user.setPassword(null);
        getHttpSession().setAttribute("user", user);
    }

    public String getIdNumber() {
        if (isUserLogin())
            return ((User) getHttpSession().getAttribute("user")).getNo();
        return "HAVEN'T LOGIN";
    }

    public User getUser() {
        return (User) getHttpSession().getAttribute("user");
    }


    public boolean isUserLogin() {
        return !Objects.isNull(getHttpSession().getAttribute("user"));
    }

    public boolean isMobile() {
        return getHttpRequest().getHeader("agent") != null && getHttpRequest().getHeader("agent").equals("Android");
    }

    public String getIMEI() {
        return getHttpRequest().getHeader("imei") == null ? "" : getHttpRequest().getHeader("imei");
    }

    public void setMobileSessionTimeout() {
        if (isMobile())
            getHttpSession().setMaxInactiveInterval(60 * 60 * 24 * 30);
    }

    public void logout() {
        getHttpSession().invalidate();
    }
}
