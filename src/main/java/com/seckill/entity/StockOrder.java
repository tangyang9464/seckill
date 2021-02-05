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
public class StockOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 库存ID
     */
    private Integer sid;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
