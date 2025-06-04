package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.app.vo.CommodityVO;
import com.example.entity.pojo.Commodity;

import java.math.BigDecimal;
import java.util.List;

public interface CommodityService extends IService<Commodity> {


    BigDecimal getLowestPriceByProductId(Integer productId);

    List<CommodityVO> getCommoditiesByProductId(Integer productId);

    // 增加商品库存
    String increaseCommodityStock(Integer commodityId, Integer quantity);

    // 减少商品库存
    String decreaseCommodityStock(Integer commodityId, Integer quantity);

    String changeCommodityStock(Integer commodityId, Integer quantity);

    List<CommodityVO> getCommoditiesByKeyword(String keyword);
}
