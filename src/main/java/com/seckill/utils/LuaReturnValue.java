package com.seckill.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum LuaReturnValue {
    /**
     *限购标志
     */
    LIMIT_BUY(-2L),
    //没有库存
    NO_STOCK(-1L);

    Long value;
}
