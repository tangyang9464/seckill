package com.seckill.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author ty
 * @date 2021/2/3 16:05
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CacheKey {
    /**
     *前缀盐，用于生成redis用户商品key
     */
    HASH_KEY("seckill_key"),
    LIMIT_KEY("limit_key"),
    USER_KEY("userToken");

    private String key;
}
