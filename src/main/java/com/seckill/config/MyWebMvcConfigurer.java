package com.seckill.config;

import com.seckill.handler.LoginHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * Mvc配置类
 * @author: ty
 * @create: 2021-02-05 21:06
 **/
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Resource
    private LoginHandler loginHandler;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //注册登录拦截器
//        InterceptorRegistration registration = registry.addInterceptor(loginHandler);
//        registration.addPathPatterns("/**");
//        registration.excludePathPatterns(
//                "/login/**",
//                "/static/**",
//                "/seckill/test/doSeckill",
//                "/test/**"
//        );
//    }
    /**
     *静态资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    /**
     *视图路径
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login/toLogin").setViewName("login");
    }
}
