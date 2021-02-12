package com.seckill.controller;

import com.seckill.entity.Stock;
import com.seckill.service.StockService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author: ty
 * @create: 2021-02-05 21:56
 **/
@RestController
public class GoodsController {
    @Resource
    StockService stockService;

    @RequestMapping("/goodsDetail")
    public ModelAndView toGoodsDetail(){
        Stock stock = stockService.list().get(0);
        ModelAndView modelAndView = new ModelAndView("goodsDetail");
        modelAndView.addObject("goods",stock);
        Duration duration = Duration.between(LocalDateTime.now(),stock.getStartDate());
        long remainSeconds = duration.toMillis()/1000;
        modelAndView.addObject("remainSeconds",remainSeconds);
        modelAndView.addObject("secKillStatus",remainSeconds>0?0:1);
        return modelAndView;
    }
}
