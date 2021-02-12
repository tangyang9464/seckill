package com.seckill.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seckill.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.utils.CacheKey;
import com.seckill.utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ty
 * @since 2021-02-05
 */
public interface UserService extends IService<User> {
    public User getUserByCookie(HttpServletRequest request) throws JsonProcessingException;
    /**
     * 获取验证值，用作秒杀接口验证
     * @param uid
     * 用户ID
     * @param sid
     * 商品ID
     * @return String
     * 请求非法时为null
     */
    public String getVertifyHash(String uid,Integer sid);
    /**
     * 验证请求合法性，包括用户、商品、秒杀时间、访问频率
     * @param uid
     * * 用户ID
     * @param sid
     * 商品ID
     * @return Boolean
     */
    public Boolean checkRequest(String uid,Integer sid);
    /**
     *增加用户-商品的访问次数
     * @param uid
     * @param sid
     * @return void
     */
    public void addVisitCount(String uid,Integer sid);
    /**
     *获取加密后的密码
     * @param salt
     * @param password
     * @return java.lang.String
     */
    public String getPasswordHash(String salt,String password);
}
