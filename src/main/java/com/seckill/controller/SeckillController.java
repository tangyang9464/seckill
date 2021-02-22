package com.seckill.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.util.concurrent.RateLimiter;
import com.seckill.entity.User;
import com.seckill.service.StockOrderService;
import com.seckill.service.UserService;
import com.seckill.utils.CacheKey;
import com.seckill.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @author tangyang9464
 */
@Slf4j
@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Resource
    private StockOrderService stockOrderService;
    @Resource
    private UserService userService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    /**
     *令牌桶限流,每秒10个
     */
    private static final  RateLimiter rateLimiter = RateLimiter.create(100);

    /**
     *创建秒杀订单
     * @param sid 商品ID
     * @param vertifyHash 验证值
     * @return java.lang.String
     */
    @RequestMapping("/doSeckill")
    public ResponseResult createOrder(@RequestParam(name = "sid") Integer sid,
                              @RequestParam(name = "vertifyHash") String vertifyHash,
                              HttpServletRequest request) throws JsonProcessingException {
        String uid = userService.getUserByCookie(request).getMobile();
        //没有足够令牌可用
        if(!rateLimiter.tryAcquire()){
            log.info("你被限制流了");
            return ResponseResult.error("你被限制流了");
        }
        //验证用户、商品、操作频率、秒杀时间、限购等
        userService.checkRequest(uid, sid);

        String key = CacheKey.HASH_KEY.getKey()+"_"+uid+"_"+sid;
        if(!stringRedisTemplate.opsForValue().get(key).equals(vertifyHash)){
            return ResponseResult.error("验证值非法");
        }
        //增加访问次数
        userService.addVisitCount(uid, sid);
        //减库存异步下单
        stockOrderService.createOrderWithRedis(uid,sid);
        log.info("购买成功");

        return ResponseResult.success();
    }
    /**
     * 压测接口
     * @param sid
     * @param uid
     * @return com.seckill.vo.ResponseResult
     */
    @RequestMapping("/test/doSeckill")
    public ResponseResult createOrder(@RequestParam(name = "sid") Integer sid,
                                      @RequestParam(name = "uid") String uid) {
        //没有足够令牌可用
        if(!rateLimiter.tryAcquire()){
//            log.info("你被限制流了");
            return ResponseResult.error("你被限制流了");
        }

        //增加访问次数
        userService.addVisitCount(uid, sid);
        //减库存异步下单
        stockOrderService.createOrderWithRedis(uid,sid);
        log.info("购买成功");

        return ResponseResult.success();
    }
    @RequestMapping("/getVertifyHash")
    public ResponseResult getVertifyHash(@RequestParam(value = "sid",required = false) Integer sid,
                                 HttpServletRequest request) throws JsonProcessingException {
        User user = userService.getUserByCookie(request);
        String uid = user.getMobile();
        String hash = userService.getVertifyHash(uid,sid);
        return ResponseResult.success(hash);
    }
}
