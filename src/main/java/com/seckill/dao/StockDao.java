package com.seckill.dao;

import com.seckill.entity.Stock;
import org.apache.ibatis.annotations.Select;

/**
 * @author tangyang9464
 */
public interface StockDao {
    /**
     * for update查询
     * @param id
     * 商品ID
     * @return com.seckill.entity.Stock
     */
    @Select("select * from stock where id = #{id} for update")
    public Stock getByIdForUpdate(int id);
}
