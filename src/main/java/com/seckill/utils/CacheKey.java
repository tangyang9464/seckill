package com.seckill.utils;

/**
 * @author ty
 * @date 2021/2/3 16:05
 */
public enum CacheKey {
    /**
     *前缀盐，用于生成redis用户商品key
     */
    HASH_KEY("seckill_key"),
    LIMIT_KEY("limit_key");

    private String key;

    CacheKey(String key){
        this.key=key;
    }
    public String getKey() {
        return key;
    }
}
