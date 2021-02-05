package com.seckill.service;

import com.seckill.entity.StockOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ty
 * @since 2021-02-05
 */
public interface StockOrderService extends IService<StockOrder> {
    /**
     * 通过乐观锁创建订单
     * @param sid
     * @param uid
     */
    public void createOrderByOptimistic(String uid,Integer sid);
    /**
     * 通过事务悲观锁锁创建订单
     * @param sid
     * @param uid
     * @return void
     */
    public void createOrderByPessimistic(String uid,Integer sid);
    /**
     * 通过redis扣减库存
     * @param sid
     * @param uid
     */
    public void createOrderWithRedis(String uid,Integer sid);
}
