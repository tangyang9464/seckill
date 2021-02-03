package com.seckill.service;

import com.seckill.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ty
 * @since 2021-02-03
 */
public interface UserService extends IService<User> {
    /**
     * 获取验证值，用作秒杀接口验证
     * @param uid
     * 用户ID
     * @param sid
     * 商品ID
     * @return String
     * 请求非法时为null
     */
    public String getVertifyHash(int uid,int sid);
    /**
     * 验证请求合法性，包括用户、商品、秒杀时间、访问频率
     * @param uid
     * * 用户ID
     * @param sid
     * 商品ID
     * @return Boolean
     */
    public Boolean checkRequest(int uid,int sid);
    /**
     *增加用户-商品的访问次数
     * @param uid
     * @param sid
     * @return void
     */
    public void addVisitCount(int uid,int sid);
}
