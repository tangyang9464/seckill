package com.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 库存
     */
    private Integer count;

    /**
     * 已售
     */
    private Integer sale;

    /**
     * 秒杀开始时间
     */
    private LocalDateTime startDate;

    /**
     * 乐观锁，版本号
     */
    private Integer version;


}
