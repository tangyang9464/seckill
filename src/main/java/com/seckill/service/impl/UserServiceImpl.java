package com.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seckill.entity.Stock;
import com.seckill.entity.User;
import com.seckill.exception.GlobalException;
import com.seckill.mapper.UserMapper;
import com.seckill.service.StockService;
import com.seckill.service.UserService;
import com.seckill.utils.CacheKey;
import com.seckill.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author tangyang9464
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private  StockService stockService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ObjectMapper objectMapper;

    @Override
    public String getPasswordHash(String salt, String password) {
        StringBuilder str = new StringBuilder(salt);
        str.append(password);
        return DigestUtils.md5DigestAsHex(str.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public User getUserByCookie(HttpServletRequest request) throws JsonProcessingException {
        String token = CookieUtil.getCookieValue(request, CacheKey.USER_KEY.getKey());
        String json = stringRedisTemplate.opsForValue().get(token);
        if(json==null){
            throw new GlobalException("用户信息不存在，应该是未登录");
        }
        return objectMapper.readValue(json,User.class);
    }

    @Override
    public String getVertifyHash(String uid, Integer sid) {
        checkRequest(uid,sid);

        String salt = System.currentTimeMillis()+"";
        StringBuilder str = new StringBuilder(salt);
        str.append(uid);
        str.append(sid);
        String hash = DigestUtils.md5DigestAsHex(str.toString().getBytes(StandardCharsets.UTF_8));
        //放入redis,设置3600秒
        String redisKey = CacheKey.HASH_KEY.getKey()+"_"+uid+"_"+sid;
        stringRedisTemplate.opsForValue().set(redisKey,hash,3600, TimeUnit.SECONDS);
        return hash;
    }

    @Override
    public Boolean checkRequest(String uid, Integer sid) {
        //验证用户合法性
        if(uid==null || this.getById(uid)==null){
            log.info("用户不存在");
            throw new GlobalException("用户不存在");
        }
        //验证用户操作频率
        String limitKey = CacheKey.LIMIT_KEY.getKey()+"_"+uid+"_"+sid;
        String limitValue = stringRedisTemplate.opsForValue().get(limitKey);
        if(limitValue!=null && Integer.parseInt(limitValue)>10){
            log.info("操作频率过高");
            throw new GlobalException("操作频率过高");
        }
//        //验证是否限购
//        StringBuilder str = new StringBuilder("uid_");
//        str.append(uid);
//        str.append("_");
//        str.append("sid_");
//        str.append(sid);
//        if(stringRedisTemplate.opsForValue().get(str.toString())!=null){
//            throw new GlobalException("限购");
//        }
        Stock stock = stockService.getById(sid);
        //验证商品合法性
        if(stock==null){
            log.info("商品不存在");
            throw new GlobalException("商品不存在");
        }
        //验证时间
        if(stock.getStartDate().isAfter(LocalDateTime.now())){
            log.info("未到秒杀时间");
            throw new GlobalException("未到秒杀时间");
        }


        return true;
    }

    @Override
    public void addVisitCount(String uid, Integer sid) {
        String limitKey = CacheKey.LIMIT_KEY.getKey()+"_"+uid+"_"+sid;
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(limitKey,"0",10,TimeUnit.MINUTES);
        if(flag!=null && !flag) {
            stringRedisTemplate.boundValueOps(limitKey).increment(1);
        }
    }
}
