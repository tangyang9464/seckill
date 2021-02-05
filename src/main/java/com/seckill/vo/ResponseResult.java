package com.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

/**
 * 统一返回结果
 * @author tangyang9464
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {
    private Integer code;
    private String msg;
    private Object data;

    /**
     * 成功返回结果,无数据
     */
    public static ResponseResult success() {
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMsg(), null);
    }
    /**
     * 成功返回结果
     *
     * @param obj
     */
    public static ResponseResult success(Object obj) {
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMsg(), obj);
    }
    /**
     * 失败返回结果
     *
     * @param msg
     * @return
     */
    public static ResponseResult error(String msg) {
        return new ResponseResult(ResponseCode.ERROR.getCode(), msg,null);
    }
}
