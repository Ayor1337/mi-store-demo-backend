package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderTest implements Serializable {
    /**
     * 订单Id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 接口Id
     */
    private String productName;

    /**
     * 支付金额
     */
    private Double money;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 0 - 未支付 1 - 已支付
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

}
