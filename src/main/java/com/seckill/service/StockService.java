package com.seckill.service;

import com.seckill.entity.Stock;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ty
 * @since 2021-02-02
 */
public interface StockService extends IService<Stock> {
    /**
     *forUpdate加锁
     * @param
     * @return com.seckill.entity.Stock
     */
    public Stock getByIdForUpdate(int id);
}
