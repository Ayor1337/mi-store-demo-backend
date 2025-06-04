package com.example.entity.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AddCartItemMessage implements Serializable {
    private Integer userId;
    private Integer commodityId;
    private Integer quantity;
}
