package com.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.seckill.service.StockOrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author tangyang9464
 */
@RestController
public class CreateOrderController {
    @Resource
    private StockOrderService stockOrderService;
    /**
     *令牌桶限流,每秒10个
     */
    private static final  RateLimiter rateLimiter = RateLimiter.create(10);
    /**
     *日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CreateOrderController.class);


    /**
     * 创建秒杀订单
     * @param id
     * @return void
     */
    @RequestMapping("/createOrder/{id}")
    public String createOrder(@PathVariable int id){
        //没有足够令牌可用
        if(!rateLimiter.tryAcquire()){
            logger.debug("你被限制流了");
            return "你被限制流了";
        }

        //通过悲观锁
        stockOrderService.createOrderByPessimistic(id);
        logger.debug("购买成功");
        return "OK";
    }
}
