package com.seckill.bean;

import com.seckill.entity.Stock;
import com.seckill.service.StockService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 启动时将库存加载进redis
 * @author ty
 * @date 2021/2/4 14:11
 */
@Component
public class InitializingStock implements InitializingBean{
    @Resource
    StockService stockService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Stock> stockList = stockService.list();
        if(stockList==null) {
            return;
        }
        for(Stock stock:stockList){
            stringRedisTemplate.opsForValue().set(String.valueOf(stock.getId()),String.valueOf(stock.getCount()));
        }
    }
}
