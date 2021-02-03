package com.seckill.service.impl;

import com.seckill.entity.Stock;
import com.seckill.mapper.StockMapper;
import com.seckill.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ty
 * @since 2021-02-03
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    private StockMapper stockMapper;

    @Override
    public Stock getByIdForUpdate(int id) {
        return stockMapper.getByIdForUpdate(id);
    }
}
