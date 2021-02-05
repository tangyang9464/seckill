package com.seckill.handler;

import com.seckill.exception.GlobalException;
import com.seckill.utils.CookieUtil;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.tomcat.jni.Global;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截器
 * @author tangyang9464
 */
public class LoginHandler implements HandlerInterceptor{
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String token = CookieUtil.getCookieValue(request,"userToken");
        if(token!=null && stringRedisTemplate.hasKey(token)){
            return true;
        }
        return false;
    }
}
