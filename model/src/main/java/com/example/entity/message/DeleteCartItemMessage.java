package com.example.entity.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DeleteCartItemMessage implements Serializable {
    private Integer cartItemId;
    private Integer cartId;
    private Integer quantity;
    private Integer commodityId;
}
