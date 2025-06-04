package com.example.entity.message.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC 返回的购物车添加结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemResult implements Serializable {
    /**
     * 用户 ID
     */
    private Integer userId;
    /**
     * 商品 ID
     */
    private Integer commodityId;
    /**
     * 添加是否成功
     */
    private Boolean success;
    /**
     * 可选：错误或提示信息
     */
    private String message;
}
