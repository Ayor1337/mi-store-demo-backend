package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.CommodityDTO;
import com.example.entity.pojo.Commodity;
import com.example.entity.vo.CommodityVO;
import com.example.entity.vo.CommodityWithFullNameVO;

import java.util.List;

public interface CommodityService extends IService<Commodity> {
    List<CommodityVO> getCommodityVOs();

    CommodityVO getCommodityVOById(Integer id);

    CommodityVO getCommodityById(Integer id);

    List<CommodityVO> getCommodityVOsByProductId(Integer productId);

    String deleteById(Integer id);

    String deleteCommodityByProductId(Integer productId);

    String saveCommodity(CommodityDTO dto);

    String updateCommodity(CommodityDTO dto);

    String increaseCommodityStock(Integer productId, Integer quantity);

    String decreaseCommodityStock(Integer productId, Integer quantity);

    List<CommodityWithFullNameVO> getFullNameListByProductId(Integer productId);
}
