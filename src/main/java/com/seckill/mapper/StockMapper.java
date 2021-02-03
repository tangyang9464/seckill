package com.seckill.mapper;

import com.seckill.entity.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ty
 * @since 2021-02-02
 */
public interface StockMapper extends BaseMapper<Stock> {
    /**
     * for Update悲观锁查询
     * @param id
     * @return com.seckill.entity.Stock
     */
    @Select("select * from stock where id=#{id} for update")
    public Stock getByIdForUpdate(int id);
}