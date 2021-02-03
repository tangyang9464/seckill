package com.seckill.service.impl;

import com.seckill.entity.Stock;
import com.seckill.entity.StockOrder;
import com.seckill.mapper.StockOrderMapper;
import com.seckill.service.StockOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class StockOrderServiceImpl extends ServiceImpl<StockOrderMapper, StockOrder> implements StockOrderService {
    @Resource
    private StockService stockService;
    @Resource
    private StockOrderService stockOrderService;

    @Override
    /**
     * 通过乐观锁创建订单
     * @param id
     * @return void
     */
    public void createOrderByOptimistic(int id){
        //校验库存
        Stock stock = checkStockByOptimistic(id);
        //减去库存
        saleStock(stock);
        //创建订单
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(stock.getId());
        stockOrder.setName(stock.getName());
        stockOrderService.save(stockOrder);
    }
    /**
     * 通过事务悲观锁锁创建订单
     * @param id
     * @return void
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void createOrderByPessimistic(int id) {

        //加锁校验库存
        Stock stock = checkStockByPessimistic(id);
        //减去库存
        saleStock(stock);
        //创建订单
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(stock.getId());
        stockOrder.setName(stock.getName());
        stockOrderService.save(stockOrder);
    }

    /**
     * 校验库存
     * @param id
     * 库存id
     * @return com.seckill.entity.Stock
     */
    public Stock checkStockByOptimistic(int id){
        Stock stock = stockService.getById(id);
        if(stock.getCount()<=0) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }
    /**
     * 悲观锁校验库存
     * @param id
     * 库存id
     * @return com.seckill.entity.Stock
     */
    public Stock checkStockByPessimistic(int id){
        Stock stock = stockService.getByIdForUpdate(id);
        if(stock.getCount()<=0) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }
    /**
     *乐观锁减少库存
     * @param stock
     * 库存实体类
     * @return void
     */
    public void saleStock(Stock stock){
        stock.setSale(stock.getSale()+1);
        stock.setCount(stock.getCount()-1);
        boolean res = stockService.updateById(stock);
        if(!res){
            throw new RuntimeException("乐观锁更新库存失败");
        }
    }
}
