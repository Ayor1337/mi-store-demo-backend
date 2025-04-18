package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Base64Upload;
import com.example.entity.dto.ProductDTO;
import com.example.entity.pojo.Product;
import com.example.entity.vo.ProductVO;

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
}
