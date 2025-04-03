package com.example.entity.vo.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

//@Builder
@Data
public class RespProductVO {

        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
        private Integer categoryId;

}
