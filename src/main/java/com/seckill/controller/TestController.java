package com.seckill.controller;

import com.seckill.service.StockOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author: tangyang9464
 * @create: 2021-02-21 11:03
 **/
@Controller
public class TestController {
    @Resource
    StockOrderService stockOrderService;

    @RequestMapping("/test")
    public void test(){
        stockOrderService.createOrderByPessimistic("1",1);
    }
}
