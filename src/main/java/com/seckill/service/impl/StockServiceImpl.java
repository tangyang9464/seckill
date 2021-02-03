package com.seckill.service.impl;

import com.seckill.entity.Stock;
import com.seckill.mapper.StockMapper;
import com.seckill.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2021-02-02
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    @Resource
    private StockMapper stockMapper;

    @Override
    public Stock getByIdForUpdate(int id) {
        return stockMapper.getByIdForUpdate(id);
    }
}
