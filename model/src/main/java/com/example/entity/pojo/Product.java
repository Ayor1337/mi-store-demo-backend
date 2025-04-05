package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("Products")
@AllArgsConstructor
@NoArgsConstructor  
public class Product implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer productId;    // 商品ID
    private String name;          // 商品名称
    private String description;   // 商品描述
    private BigDecimal price;     // 价格
    private Integer stock;        // 库存数量
    private Integer categoryId;   // 所属分类ID
    private String imageUrl;      // 商品图片地址
    private Date createTime;      // 添加时间
}
