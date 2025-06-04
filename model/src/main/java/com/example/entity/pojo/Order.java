package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("Orders")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Order", description = "订单信息")
public class Order implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "订单ID")
    private Integer orderId;         // 订单ID

    @Schema(description = "用户ID")
    private Integer userId;          // 用户ID

    @Schema(description = "订单号")
    private BigDecimal totalPrice;   // 订单总金额

    @Schema(description = "订单状态")
    private String status;           // 订单状态（如待支付、待发货、已发货、已完成、已取消）

    @Schema(description = "支付状态")
    private String paymentStatus;    // 支付状态

    @Schema(description = "配送地址")
    private Integer addressId;  // 配送地址

    @Schema(description = "下单时间")
    private Date createTime;         // 下单时间

    @Schema(description = "订单更新时间")
    private Date updateTime;         // 订单更新时间
    
}
