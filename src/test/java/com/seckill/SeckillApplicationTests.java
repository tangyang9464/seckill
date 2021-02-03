package com.seckill;

import com.seckill.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeckillApplicationTests {
    @Autowired
    private StockService stockService;

    @Test
    void contextLoads() {
        System.out.println(stockService.getById(1));;
    }

}
