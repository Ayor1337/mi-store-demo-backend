package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.app.vo.CommodityVO;
import com.example.entity.pojo.Commodity;
import com.example.mapper.CommodityMapper;
import com.example.service.CommodityService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @Override
    public BigDecimal getLowestPriceByProductId(Integer productId) {
        Commodity commodity = this.lambdaQuery()
                .eq(Commodity::getProductId, productId)
                .orderByAsc(Commodity::getPrice)
                .last("limit 1")
                .one();
        if (commodity == null) {
            return null;
        }

        return commodity.getPrice();

    }

    @Override
    public List<CommodityVO> getCommoditiesByProductId(Integer productId) {
        List<Commodity> list = this.lambdaQuery().eq(Commodity::getProductId, productId).list();
        List<CommodityVO> voList = new ArrayList<>();

        for (Commodity commodity : list) {
            CommodityVO commodityVO = new CommodityVO();
            BeanUtils.copyProperties(commodity, commodityVO);
            if (commodity.getImages() != null)
                commodityVO.setImages(commodity.getImages().split(","));
            voList.add(commodityVO);
        }
        return voList;
    }

    // 增加商品库存
    @Override
    public String increaseCommodityStock(Integer commodityId, Integer quantity) {
        Commodity commodity = this.getById(commodityId);
        if (quantity == null || quantity <= 0) {
            return "购物数量必须大于0";
        }
        if (commodity == null) {
            return "商品不存在";
        }
        commodity.setStock(commodity.getStock() + quantity);
        this.updateById(commodity);
        return null;
    }

    // 减少商品库存
    @Override
    public String decreaseCommodityStock(Integer commodityId, Integer quantity) {
        Commodity commodity = this.getById(commodityId);
        if (quantity == null || quantity <= 0) {
            return "购物数量必须大于0";
        }
        if (commodity == null) {
            return "商品不存在";
        }
        if (commodity.getStock() < quantity) {
            return "库存不足";
        }
        commodity.setStock(commodity.getStock() - quantity);
        this.updateById(commodity);
        return null;
    }

    @Override
    public String changeCommodityStock(Integer commodityId, Integer quantity) {
        Commodity commodity = this.getById(commodityId);
        if (quantity == null || quantity <= 0) {
            return "购物数量必须大于0";
        }
        if (commodity == null) {
            return "商品不存在";
        }
        commodity.setStock(quantity);
        this.updateById(commodity);
        return null;
    }

}
