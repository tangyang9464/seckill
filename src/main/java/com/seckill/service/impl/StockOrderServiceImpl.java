package com.seckill.service.impl;

import com.seckill.entity.Stock;
import com.seckill.entity.StockOrder;
import com.seckill.mapper.StockOrderMapper;
import com.seckill.service.StockOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.service.StockService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;

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
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void createOrderWithRedis(Integer id) {
        //校验并扣减redis库存
        saleStockWithRedis(id);
    }

    public void saleStockWithRedis(Integer id){
        // 执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/saleStock.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(id.toString()));
        if(result==-1) {
            throw new RuntimeException("redis库存不足，扣减失败");
        }
    }


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
     * 悲观锁校验redis库存
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
