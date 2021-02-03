package com.seckill.service;

import com.seckill.entity.Stock;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ty
 * @since 2021-02-03
 */
public interface StockService extends IService<Stock> {
    /**
     * for update查询
     * @param id
     * 商品ID
     * @return com.seckill.entity.Stock
     */
    public Stock getByIdForUpdate(int id);
}
