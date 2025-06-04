package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.admin.dto.Base64Upload;
import com.example.entity.admin.dto.ProductDTO;
import com.example.entity.admin.vo.ProductVO;
import com.example.entity.pojo.Product;

import java.util.List;

public interface ProductService extends IService<Product> {
    List<ProductVO> listProduct();

    List<ProductVO> getProductByCategoryId(Integer categoryId);

    String saveProduct(ProductDTO vo);

    String deleteProductById(Integer id);

    ProductVO getProductById(Integer id);

    String updateProduct(ProductDTO vo);

    String updateImg(Integer productId, Base64Upload file);

    String updateImgUrl(Integer productId, String fileUrl);

    String deleteImg(Integer productId);

    String deleteImgUrl(Integer productId);

    /**
     * 根据关键词搜索商品，支持商品名称、描述及分类名称匹配
     *
     * @param keyword 查询关键词
     * @return 匹配的商品列表
     */
    List<ProductVO> searchProducts(String keyword);
}
