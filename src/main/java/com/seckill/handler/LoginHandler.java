package com.seckill.handler;

import com.seckill.utils.CookieUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截器
 * @author tangyang9464
 */
@Component
public class LoginHandler implements HandlerInterceptor{
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        String token = CookieUtil.getCookieValue(request,"userToken");
        if(token!=null && stringRedisTemplate.hasKey(token)){
            return true;
        }
        response.sendRedirect(request.getContextPath()+"/login/toLogin");
        return false;
    }
}
