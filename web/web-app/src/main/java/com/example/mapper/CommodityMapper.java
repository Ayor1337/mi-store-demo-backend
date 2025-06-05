package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.pojo.Commodity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommodityMapper extends BaseMapper<Commodity> {

    @Select("""
            SELECT *
            FROM Commodities
            INNER JOIN Products
                ON Commodities.product_id = Products.product_id
            INNER JOIN Categories
                ON Products.category_id = Categories.category_id
            WHERE Commodities.sku            LIKE CONCAT('%', #{keyword}, '%')
               OR Commodities.specifications LIKE CONCAT('%', #{keyword}, '%')
               OR Commodities.description    LIKE CONCAT('%', #{keyword}, '%')
               OR Products.description       LIKE CONCAT('%', #{keyword}, '%')
               OR Categories.name            LIKE CONCAT('%', #{keyword}, '%')
            """)
    List<Commodity> searchByKeyword(String keyword);


}
