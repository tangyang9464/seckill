package com.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ty
 * @since 2021-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    @TableId(value = "mobile", type = IdType.INPUT)
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;


}
