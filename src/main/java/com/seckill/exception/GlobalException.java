package com.seckill.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局异常类
 * @author tangyang9464
 */
@Data
public class GlobalException extends RuntimeException{
    public GlobalException(String msg){
        super(msg);
    }
}
