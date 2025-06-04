package com.example.entity.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCartItemMessage {
    Integer cartItemId;
    Integer cartId;
    Integer quantity;
}
