package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Base64Upload;
import com.example.entity.dto.ProductDTO;
import com.example.entity.pojo.Product;
import com.example.entity.vo.ProductVO;
import com.example.mapper.ProductMapper;
import com.example.minio.MinioService;
import com.example.service.CartItemService;
import com.example.service.CommodityService;
import com.example.service.ProductService;
import com.example.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private MinioService minioService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private CartItemService cartItemService;

    @Override
    public List<ProductVO> listProduct() {
        List<Product> list = this.list();
        return list.stream().map(product -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            return productVO;
        }).toList();
    }

    @Override
    public List<ProductVO> getProductByCategoryId(Integer categoryId) {
        List<ProductVO> voList = new ArrayList<>();
        List<Product> list = this.lambdaQuery().eq(Product::getCategoryId, categoryId).list();
        list.forEach(product -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            voList.add(productVO);
        });

        return voList;
    }

    @Override
    public String saveProduct(ProductDTO dto) {
        if (dto == null) {
            return "发送数据为空";
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategoryId(dto.getCategoryId());
        Base64Upload image = new Base64Upload();
        image.setBase64(dto.getBase64());
        image.setFileName(dto.getFileName());

        if (image.getBase64() != null && image.getFileName() != null) {
            try {
                String url = minioService.uploadBase64(image);
                product.setImageUrl(url);
            } catch (Exception e) {
                log.error("上传图片失败", e);
                return "内部发生错误";
            }
        }
        this.save(product);
        return null;
    }

    @Override
    public String deleteById(Integer id) {
        if (!existProductById(id)) {
            return "商品不存在";
        }
        cartItemService.deleteAllCartItemByProductId(id);
        commodityService.deleteCommodityByProductId(id);
        this.removeById(id);
        return null;
    }

    public boolean existProductById(Integer id) {
        return this.exists(Wrappers.<Product>lambdaQuery().eq(Product::getProductId, id));
    }

    @Override
    public ProductVO getProductById(Integer id) {
        Product product = this.lambdaQuery().eq(Product::getProductId, id).one();
        if (product == null) {
            return null;
        }
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }

    @Override
    public String updateProduct(ProductDTO dto) {
        if (dto == null) {
            return "修改的对象为空";
        }
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategoryId(dto.getCategoryId());
        this.updateById(product);

        return null;
    }

    @Override
    public String updateImg(Integer productId, Base64Upload file) {
        if (file == null) {
            return "图片为空";
        }
        try {
            String url = minioService.uploadBase64(file);
            return updateImgUrl(productId, url);
        } catch (Exception e) {
            log.error("上传图片失败", e);
            return "内部发生错误";
        }
    }

    @Override
    public String updateImgUrl(Integer productId, String fileUrl) {
        Product product = this.getById(productId);
        if (product == null) {
            return "商品不存在";
        }
        product.setImageUrl(fileUrl);
        this.updateById(product);
        return null;
    }

    @Override
    public String deleteImg(Integer productId) {
        String url = getProductById(productId).getImageUrl();
        if (url == null)
            return "商品图片不存在";
        String imageName = ImageUtil.extractObjectName(url);
        try {
            minioService.deleteFile(imageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return deleteImgUrl(productId);
    }

    @Override
    public String deleteImgUrl(Integer productId) {
        if (!existProductById(productId)) {
            return "商品不存在";
        }
        if (this.lambdaQuery().eq(Product::getProductId, productId).one().getImageUrl() == null) {
            return "商品图片不存在";
        }
        this.lambdaUpdate().eq(Product::getProductId, productId).set(Product::getImageUrl, null).update();
        return null;
    }
}
