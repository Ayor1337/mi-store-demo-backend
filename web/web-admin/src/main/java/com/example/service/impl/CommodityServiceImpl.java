package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.Base64Upload;
import com.example.entity.admin.dto.CommodityDTO;
import com.example.entity.admin.vo.CommodityVO;
import com.example.entity.admin.vo.CommodityWithFullNameVO;
import com.example.entity.pojo.Commodity;
import com.example.mapper.CommodityMapper;
import com.example.minio.MinioService;
import com.example.service.CommodityService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @Resource
    MinioService minioService;

    // 获取所有商品
    @Override
    public List<CommodityVO> getCommodityVOs() {
        List<CommodityVO> vo = new LinkedList<>();
        this.lambdaQuery().list().forEach(commodity -> {
            CommodityVO commodityVO = new CommodityVO();
            BeanUtils.copyProperties(commodity, commodityVO);
            vo.add(commodityVO);
        });
        return vo;
    }

    // 通过ID获取商品
    @Override
    public CommodityVO getCommodityById(Integer id) {
        CommodityVO commodityVO = new CommodityVO();
        Commodity commodity = this.getById(id);
        if (commodity == null) {
            return null;
        }
        // TODO 修复空数组BUG
        BeanUtils.copyProperties(commodity, commodityVO);
        if (commodity.getImages() != null) {
            String[] array = Arrays.stream(commodity.getImages().split(",")).toArray(String[]::new);
            commodityVO.setImages(array);
        }
        return commodityVO;
    }

    // 通过产品ID获取商品
    @Override
    public List<CommodityVO> getCommodityVOsByProductId(Integer productId) {
        List<CommodityVO> vo = new LinkedList<>();
        List<Commodity> list = this.lambdaQuery().eq(Commodity::getProductId, productId).list();
        if (list == null)
            return null;
        list.forEach(commodity -> {
            CommodityVO commodityVO = new CommodityVO();
            BeanUtils.copyProperties(commodity, commodityVO);
            vo.add(commodityVO);
        });
        return vo;
    }

    // 通过ID删除商品
    @Override
    public String deleteCommodityById(Integer id) {
        if (id == null) {
            return "id为空";
        }
        this.removeById(id);
        return null;
    }

    // 通过产品ID删除商品
    @Override
    public String deleteCommodityByProductId(Integer productId) {
        if (productId == null) {
            return "商品id为空";
        }
        this.lambdaUpdate().eq(Commodity::getProductId, productId).remove();
        return null;
    }

    // 保存商品
    @Override
    public String saveCommodity(CommodityDTO dto) {
        if (dto == null) {
            return "商品为空";
        }
        Commodity commodity = new Commodity();
        BeanUtils.copyProperties(dto, commodity);
        StringBuilder image_urls = uploadImages(dto);
        commodity.setImages(image_urls.toString());
        this.save(commodity);
        return null;
    }

    // 更新商品
    @Override
    public String updateCommodity(CommodityDTO dto) {
        if (dto == null) {
            return "商品为空";
        }

        Commodity commodity = this.lambdaQuery().eq(Commodity::getCommodityId, dto.getCommodityId()).one();
        BeanUtils.copyProperties(dto, commodity);
        StringBuilder image_urls = uploadImages(dto);
        commodity.setImages(image_urls.toString());
        this.updateById(commodity);
        return null;
    }

    // 上传图片
    @NotNull
    private StringBuilder uploadImages(CommodityDTO dto) {
        StringBuilder image_urls = new StringBuilder();
        Map<String, String> images = dto.getImages();
        if (!images.isEmpty()) {
            images.forEach((fileName, base64url) -> {
                Base64Upload base64Upload = new Base64Upload(base64url, fileName);
                try {
                    String s = minioService.uploadBase64(base64Upload);
                    image_urls.append(s).append(",");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return image_urls;
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

    // 通过产品ID获取商品全称
    @Override
    public List<CommodityWithFullNameVO> getFullNameListByProductId(Integer productId) {
        List<CommodityWithFullNameVO> list = new LinkedList<>();
        this.lambdaQuery().eq(Commodity::getProductId, productId).list().forEach(commodity -> {
            CommodityVO commodityVO = new CommodityVO();
            BeanUtils.copyProperties(commodity, commodityVO);
            CommodityWithFullNameVO withFullNameVO = new CommodityWithFullNameVO();
            withFullNameVO.setCommodityId(commodity.getCommodityId());
            withFullNameVO.setFullName(commodityVO.getFullName());
            list.add(withFullNameVO);
        });
        return list;
    }

//    @Override
//    public String uploadImage(Integer commodityId, Base64Upload[] files) {
//        if (files.length == 0) {
//            return "上传文件为空";
//        }
//        Commodity commodity = this.getById(commodityId);
//        StringBuilder images = new StringBuilder();
//        for (int i = 0; i < files.length; i++) {
//            try {
//                String s = minioService.uploadBase64(files[i]);
//                images.append(s);
//                if (i < files.length - 1) {
//                    images.append(",");
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        commodity.setImages(images.toString());
//        this.updateById(commodity);
//        return null;
//    }


}
