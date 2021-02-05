package com.seckill.config;

import com.seckill.handler.LoginHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 登录拦截器配置
 * @author: ty
 * @create: 2021-02-05 21:06
 **/
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginHandler());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns(
                "/login/**"
        );
    }
}
