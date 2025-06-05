package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@TableName(value = "Commodities", autoResultMap = true)
@Schema(name = "Commodity", description = "商品信息")
public class Commodity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "商品ID")
    private Integer commodityId;

    @Schema(description = "商品所属产品ID")
    private Integer productId;

    @Schema(description = "商品SKU")
    private String sku;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "商品库存")
    private Integer stock;

    @Schema(description = "商品价格")
    private BigDecimal price;

    @Schema(description = "商品规格")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> specifications = new HashMap<>();

    @Schema(description = "商品图片")
    private String images;

    @Schema(description = "获取商品全称")
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(sku).append(" ");
        if (specifications != null && !specifications.isEmpty()) {
            specifications.forEach((k, v) ->
                    sb.append(v).append(" ")
            );
        }
        return sb.toString();
    }
}
