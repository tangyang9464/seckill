package com.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.dao.StockDao;
import com.seckill.entity.Stock;
import com.seckill.mapper.StockMapper;
import com.seckill.service.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tangyang9464
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    @Resource
    private StockMapper stockMapper;

    @Override
    public Stock getByIdForUpdate(Integer id) {
        return stockMapper.selectOne(new QueryWrapper<Stock>()
                .eq("id",1)
                .last("for update"));
    }
}
