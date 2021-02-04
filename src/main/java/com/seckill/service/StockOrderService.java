package com.seckill.service;

import com.seckill.entity.StockOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ty
 * @since 2021-02-02
 */
public interface StockOrderService extends IService<StockOrder> {
    /**
     * 通过乐观锁创建订单
     * @param id
     * @return void
     */
    public void createOrderByOptimistic(int id);
    /**
     * 通过事务悲观锁锁创建订单
     * @param id
     * @return void
     */
    public void createOrderByPessimistic(int id);
    /**
     * 通过redis扣减库存
     * @param id
     * @return void
     */
    public void createOrderWithRedis(Integer id);
}
