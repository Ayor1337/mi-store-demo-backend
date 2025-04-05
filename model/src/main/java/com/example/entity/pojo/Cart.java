package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("Cart")
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer cartId;       // 购物车ID
    private Integer userId;       // 用户ID
    private Date createTime;      // 购物车创建时间
}
