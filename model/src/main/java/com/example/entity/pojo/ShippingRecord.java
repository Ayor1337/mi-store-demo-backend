package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("ShippingRecords")
@AllArgsConstructor
@NoArgsConstructor
public class ShippingRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    @Schema(description = "配送记录ID")
    private Integer shippingId;      // 配送记录ID

    @Schema(description = "订单ID")
    private Integer orderId;         // 订单ID

    @Schema(description = "物流公司")
    private String shippingCompany;  // 物流公司

    @Schema(description = "物流单号")
    private String trackingNumber;   // 快递单号

    @Schema(description = "发货时间")
    private Date shippingTime;       // 发货时间

    @Schema(description = "签收时间")
    private Date deliveryTime;       // 签收时间
}
