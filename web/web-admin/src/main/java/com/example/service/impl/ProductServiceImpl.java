package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.Base64Upload;
import com.example.entity.admin.dto.ProductDTO;
import com.example.entity.admin.vo.ProductVO;
import com.example.entity.pojo.Product;
import com.example.mapper.ProductMapper;
import com.example.minio.MinioService;
import com.example.service.CartItemService;
import com.example.service.CommodityService;
import com.example.service.ProductService;
import com.example.service.CategoryService;
import com.example.util.ImageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private MinioService minioService;

    @Resource
    private CommodityService commodityService;

    @Resource
    private CartItemService cartItemService;

    @Resource
    private CategoryService categoryService;


    // 获取商品列表
    @Override
    public List<ProductVO> listProduct() {
        List<Product> list = this.list();
        return list.stream().map(product -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            return productVO;
        }).toList();
    }

    // 通过ID获取商品
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

    // 根据分类ID获取商品列表
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

    // 根据关键词搜索商品，匹配名称、描述及分类名称
    @Override
    public List<ProductVO> searchProducts(String keyword) {
        List<Integer> categoryIds = categoryService.searchCategoryIds(keyword);
        List<Product> list = this.lambdaQuery()
                .and(wrapper -> wrapper.like(Product::getName, keyword)
                        .or()
                        .like(Product::getDescription, keyword))
                .or()
                .in(!categoryIds.isEmpty(), Product::getCategoryId, categoryIds)
                .list();
        return list.stream().map(product -> {
            ProductVO vo = new ProductVO();
            BeanUtils.copyProperties(product, vo);
            return vo;
        }).toList();
    }

    // 保存商品
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

    // 更新商品
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

    // 通过ID删除商品
    @Override
    public String deleteProductById(Integer id) {
        if (!existProductById(id)) {
            return "商品不存在";
        }
        final String messageFormDeleteAllCartItemByProductId = cartItemService.deleteAllCartItemByProductId(id);
        if (messageFormDeleteAllCartItemByProductId != null) {
            return messageFormDeleteAllCartItemByProductId;
        }

        final String messageFormDeleteCommodityByProductId = commodityService.deleteCommodityByProductId(id);
        if (messageFormDeleteCommodityByProductId != null) {
            return messageFormDeleteCommodityByProductId;
        }

        this.removeById(id);
        return null;
    }

    // Minio上传更新商品图片
    @Override
    public String updateImg(Integer productId, Base64Upload file) {
        if (file == null) {
            return "图片为空";
        }
        try {
            String url = minioService.uploadBase64(file);
            System.out.println(url);
            return updateImgUrl(productId, url);
        } catch (Exception e) {
            log.error("上传图片失败", e);
            return "内部发生错误";
        }
    }

    // 数据库更新商品图片URL
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

    // 删除商品图片
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

    // 删除商品图片URL
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

    // 判断商品是否存在
    public boolean existProductById(Integer id) {
        return this.exists(Wrappers.<Product>lambdaQuery().eq(Product::getProductId, id));
    }
}
