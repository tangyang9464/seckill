package com.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tangyang9464
 */

@Getter
@AllArgsConstructor
public enum ResponseCode {
    //通用状态码
    SUCCESS(200,"success"),
    ERROR(500,"error");
    //登录状态码


    private Integer code;
    private String msg;
}
