package project.ys.glass_system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import project.ys.glass_system.model.dto.RetResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static project.ys.glass_system.constant.HttpConstant.*;


public class AuthorityInterceptor implements HandlerInterceptor {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI();

        AntPathMatcher pathMatcher = new AntPathMatcher();

        HandlerMethod handlerMethod = null;
        if (handler instanceof HandlerMethod)
            handlerMethod = (HandlerMethod) handler;

        //检查是否有Unlimited注解
        if (!Objects.isNull(handlerMethod) && (handlerMethod.hasMethodAnnotation(Unlimited.class)
                || handlerMethod.getMethod().getDeclaringClass().getDeclaredAnnotation(Unlimited.class) != null)) {

            //防止重复登陆
            if (SessionUtil.getInstance().isUserLogin() && pathMatcher.match(LOGIN, requestUrl)) {
                return false;
            }

            return true;
        }


        //检查url是否为静态资源文件
        if (pathMatcher.match("/static/**", requestUrl))
            return true;


        //检查是否登录
        if (SessionUtil.getInstance().isUserLogin()) {
            //首页
            if (pathMatcher.match("/", requestUrl)
                    || pathMatcher.match(HOME, requestUrl)
                    || pathMatcher.match(USER + USER_MANAGER, requestUrl)
                    || pathMatcher.match(REDIRECT, requestUrl))
                return true;


            return false;
        }

        //未登录
        if (!Objects.isNull(handlerMethod)) {
            if ((handlerMethod.getMethod().getReturnType().equals(RetResponse.class)) || SessionUtil.getInstance().isMobile())
                response.getWriter().write(objectMapper.writeValueAsString(RetResponse.makeErrRsp("need to login")));
            else
                response.sendRedirect(LOGIN);
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }
}
