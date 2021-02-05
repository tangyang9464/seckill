package com.seckill.handler;

import com.seckill.exception.GlobalException;
import com.seckill.vo.ResponseCode;
import com.seckill.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 * @author ty
 * @date 2021/2/3 11:06
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        log.info(e.getMessage());
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException)e;
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.error(e.getMessage());
    }
}
