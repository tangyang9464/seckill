package com.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.entity.Stock;
import com.seckill.entity.StockOrder;
import com.seckill.exception.GlobalException;
import com.seckill.mapper.StockOrderMapper;
import com.seckill.service.StockOrderService;
import com.seckill.service.StockService;
import com.seckill.utils.LuaReturnValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author tangyang9464
 */
@Service
@Slf4j
public class StockOrderServiceImpl extends ServiceImpl<StockOrderMapper, StockOrder> implements StockOrderService {
    @Resource
    private StockService stockService;
    @Resource
    private StockOrderService stockOrderService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    KafkaTemplate kafkaTemplate;

    HashMap<Integer,Boolean> isEmpty = new HashMap<>();

    @Override
    public void createOrderWithRedis(String uid, Integer sid) {
        //校验并扣减redis库存并将订单id放入异步队列
        saleStockWithRedis(uid,sid);
    }
    /**
     *校验并扣减redis库存并将订单id放入异步队列
     * @param sid 商品ID
     * @return void
     */
    public void saleStockWithRedis(String uid,Integer sid){
        Boolean flag = isEmpty.getOrDefault(sid,false);
        //库存为空
        if(flag){
            throw new GlobalException("redis库存不足，扣减失败");
        }

        // 执行 lua 脚本扣减库存
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/static/lua/saleStock.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        List<String> params = new ArrayList<>();
        params.add(sid.toString());
        StringBuilder s = new StringBuilder("uid_");
        s.append(uid);
        s.append("_");
        s.append("sid_");
        s.append(sid);
        String uid_sid = s.toString();
        params.add(uid_sid);
        Long result = stringRedisTemplate.execute(redisScript, params);
        if(result.equals(LuaReturnValue.NO_STOCK.getValue()) ) {
            //内存标记1
            isEmpty.put(sid,true);
            throw new RuntimeException("redis库存不足，扣减失败");
        }
        else if(result.equals(LuaReturnValue.LIMIT_BUY.getValue())){
            throw new RuntimeException("用户限购一件，扣减失败");
        }
        else{
            String str = uid+"_"+sid;
            stringRedisTemplate.opsForValue().set(uid_sid,"true");
            //回调函数重试
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("seckill", str);
            future.addCallback(success -> log.info("成功，生产者发送消息"),
                    fail -> {
                        log.info("失败，生产者发送消息，原因：{}",fail.getMessage());
                        kafkaTemplate.send("seckill", str);
                    });
        }
    }
    @KafkaListener(topics = "seckill",groupId = "g1",containerFactory="batchFactory")
    public void consumer(ConsumerRecord<String,String> msg, Acknowledgment ack){
        String[] s = msg.value().split("_");
        String uid = s[0];
        Integer sid = Integer.parseInt(s[1]);
        //异步创建订单
        createOrderByPessimistic(uid,sid);
        //手动提交
        ack.acknowledge();
        log.info("uid:"+uid+"__"+"消费信息");
    }


    /**
     * 通过乐观锁创建订单
     * @param sid
     * @return void
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void createOrderByOptimistic(String uid,Integer sid){
        //校验库存
        Stock stock = checkStockByOptimistic(sid);
        //减去库存
        saleStock(stock);
        //创建订单
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(sid);
        stockOrder.setName(stock.getName());
        stockOrder.setUid(uid);
        stockOrderService.save(stockOrder);
    }
    /**
     * 通过事务悲观锁锁创建订单
     * @param sid
     * @return void
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void createOrderByPessimistic(String uid,Integer sid) {

        //加锁校验库存
        Stock stock = checkStockByPessimistic(sid);
        //减去库存
        saleStock(stock);
        //创建订单
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(stock.getId());
        stockOrder.setName(stock.getName());
        stockOrder.setUid(uid);
        stockOrderService.save(stockOrder);
    }

    /**
     * 乐观锁校验库存
     * @param sid
     * 库存id
     * @return com.seckill.entity.Stock
     */
    public Stock checkStockByOptimistic(Integer sid){
        Stock stock = stockService.getById(sid);
        if(stock.getCount()<=0) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }
    /**
     * 悲观锁校验库存
     * @param sid
     * 库存id
     * @return com.seckill.entity.Stock
     */
    public Stock checkStockByPessimistic(Integer sid){
        Stock stock = stockService.getByIdForUpdate(sid);
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
