package com.seckill.service.impl;

import com.seckill.entity.Stock;
import com.seckill.entity.User;
import com.seckill.mapper.UserMapper;
import com.seckill.service.StockService;
import com.seckill.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2021-02-03
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private StockService stockService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String getVertifyHash(int uid, int sid) {
        if(!checkRequest(uid,sid)){
            return null;
        }

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
    public Boolean checkRequest(int uid, int sid) {
        //验证商品合法性
        Stock stock = stockService.getById(sid);
        if(stock==null){
            log.info("商品不存在");
            return false;
        }
        //验证时间
        if(LocalDateTime.now().isBefore(LocalDateTime.now())){
            log.info("未到秒杀时间");
            return false;
        }
        //验证用户合法性
        if(this.getById(uid)==null){
            log.info("用户不存在");
            return false;
        }
        //验证用户操作频率
        String limitKey = CacheKey.LIMIT_KEY.getKey()+"_"+uid+"_"+sid;
        String limitValue = stringRedisTemplate.opsForValue().get(limitKey);
        if(limitValue!=null && Integer.parseInt(limitKey)>10){
            log.info("操作频率过高");
            return false;
        }
        return true;
    }

    @Override
    public void addVisitCount(int uid, int sid) {
        String limitKey = CacheKey.LIMIT_KEY.getKey()+"_"+uid+"_"+sid;
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(limitKey,"0",10,TimeUnit.MINUTES);
        if(flag!=null && !flag) {
            stringRedisTemplate.boundValueOps(limitKey).increment(1);
        }
    }
}
