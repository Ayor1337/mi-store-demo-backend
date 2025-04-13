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
@TableName("PaymentRecords")
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PaymentRecord", description = "支付记录信息")
public class PaymentRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "支付记录ID")
    private Integer paymentId;       // 支付记录ID

    @Schema(description = "订单ID")
    private Integer orderId;         // 订单ID

    @Schema(description = "支付状态")
    private String paymentMethod;    // 支付方式（如支付宝、微信、信用卡等）

    @Schema(description = "支付金额")
    private BigDecimal amount;       // 支付金额

    @Schema(description = "支付时间")
    private Date paymentTime;        // 支付时间

    @Schema(description = "第三方支付交易号")
    private String transactionId;    // 第三方支付交易号
}
