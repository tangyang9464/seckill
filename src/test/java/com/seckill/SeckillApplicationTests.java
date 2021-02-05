package com.seckill;

import com.seckill.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SeckillApplicationTests {
    @Resource
    StockService stockService;

    @Test
    public void afterPropertiesSet() throws Exception {
        stockService.list();
    }
}
