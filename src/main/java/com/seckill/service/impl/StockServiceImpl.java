package com.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.dao.StockDao;
import com.seckill.entity.Stock;
import com.seckill.mapper.StockMapper;
import com.seckill.service.StockService;
import org.springframework.stereotype.Service;

/**
 * @author tangyang9464
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    private StockDao stockDao;

    @Override
    public Stock getByIdForUpdate(int id) {
        return stockDao.getByIdForUpdate(id);
    }
}
