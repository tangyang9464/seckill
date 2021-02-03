package com.seckill;

import com.seckill.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class SeckillApplicationTests {
    @Autowired
    private StockService stockService;
    @Resource
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("mykey","星宇");
        System.out.println(redisTemplate.opsForValue().get("mykey"));
    }

}
