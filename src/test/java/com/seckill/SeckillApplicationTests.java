package com.seckill;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.entity.Stock;
import com.seckill.mapper.StockMapper;
import com.seckill.service.StockService;
import com.seckill.service.impl.StockOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SeckillApplicationTests {
    @Resource
    StockOrderServiceImpl stockOrderService;

    @Test
    public void afterPropertiesSet() throws Exception {
        stockOrderService.createOrderByPessimistic("1",1);
    }
}
