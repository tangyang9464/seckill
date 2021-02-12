package com.seckill.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seckill.entity.User;
import com.seckill.service.UserService;
import com.seckill.vo.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author tangyang9464
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    UserService userService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ObjectMapper objectMapper;

    @RequestMapping("/doLogin")
    public ResponseResult doLogin(@RequestParam String mobile,
                                  @RequestParam String password,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws JsonProcessingException {
        User user =  userService.getById(mobile);
        if(user==null){
            return ResponseResult.error("手机号不存在");
        }
        String nowPassword = userService.getPasswordHash(user.getSalt(),password);
        if(!StringUtils.equals(nowPassword,user.getPassword())){
            return ResponseResult.error("密码错误");
        }
        String userToken = UUID.randomUUID().toString();
        Cookie cookie=new Cookie("userToken",userToken);
        cookie.setPath("/");
        response.addCookie(cookie);
        stringRedisTemplate.opsForValue().set(userToken,objectMapper.writeValueAsString(user),10, TimeUnit.DAYS);
        return ResponseResult.success();
    }
    @RequestMapping("/doRegister")
    public ResponseResult doRegister(@RequestParam String mobile,
                                  @RequestParam String password,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        String salt = System.currentTimeMillis()+"";
        String nowPassword = userService.getPasswordHash(salt,password);
        User user =  userService.getById(mobile);
        if(user!=null){
            return ResponseResult.error("手机号已存在");
        }
        user = new User();
        user.setMobile(mobile);
        user.setPassword(nowPassword);
        user.setSalt(salt);
        userService.save(user);
        return ResponseResult.success();
    }
}
