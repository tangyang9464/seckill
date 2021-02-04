package com.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.seckill.service.StockOrderService;
import com.seckill.service.UserService;
import com.seckill.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * @author tangyang9464
 */
@Slf4j
@RestController
public class CreateOrderController {
    @Resource
    private StockOrderService stockOrderService;
    @Resource
    private UserService userService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    /**
     *令牌桶限流,每秒10个
     */
    private static final  RateLimiter rateLimiter = RateLimiter.create(10);

    /**
     *创建秒杀订单
     * @param sid 商品ID
     * @param uid 用户ID
     * @param vertifyHash 验证值
     * @return java.lang.String
     */
    @RequestMapping("/createOrder")
    public String createOrder(@RequestParam int sid,
                              @RequestParam int uid,
                              @RequestParam String vertifyHash){
        //没有足够令牌可用
        if(!rateLimiter.tryAcquire()){
            log.debug("你被限制流了");
            return "你被限制流了";
        }
        //验证用户、商品、操作频率、秒杀时间等
        if(!userService.checkRequest(uid, sid)){
            return "请求非法";
        }

        String key = CacheKey.HASH_KEY.getKey()+"_"+uid+"_"+sid;
        if(stringRedisTemplate.opsForValue().get(key)!=vertifyHash){
            log.debug("验证值非法");
            return"";
        }
        //增加访问次数
        userService.addVisitCount(uid, sid);
        //通过悲观锁
        stockOrderService.createOrderByPessimistic(sid);
        log.debug("购买成功");
        return "OK";
    }
    @RequestMapping("/getVertifyHash")
    public String getVertifyHash(@RequestParam(value = "uid") int uid,
                                 @RequestParam(value = "sid") int sid){
        return userService.getVertifyHash(uid,sid);
    }
}
