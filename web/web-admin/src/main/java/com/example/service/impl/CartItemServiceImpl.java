package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.CartItemDTO;
import com.example.entity.pojo.CartItem;
import com.example.entity.pojo.Product;
import com.example.entity.vo.CartItemVO;
import com.example.mapper.CartItemMapper;
import com.example.service.CartItemService;
import com.example.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

    @Autowired
    ProductService productService;

    @Override
    public List<CartItemVO> getCartItemVOByCartId(Integer cartId) {
        List<CartItemVO> cartItemVOList = new LinkedList<>();

        this.lambdaQuery()
                .eq(CartItem::getCartId, cartId)
                .list()
                .forEach(cartItem -> {
                    CartItemVO cartItemVO = new CartItemVO();
                    cartItemVO.setCartItemId(cartItem.getCartItemId());
                    cartItemVO.setName(productService.getById(cartItem.getProductId()).getName());
                    cartItemVO.setPrice(productService.getById(cartItem.getProductId()).getPrice());
                    cartItemVO.setQuantity(cartItem.getQuantity());
                    cartItemVO.setTotalPrice(cartItemVO.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                    cartItemVOList.add(cartItemVO);
                });
        return cartItemVOList;
    }

    @Override
    @Transactional
    public String saveCartItem(CartItemDTO dto) {
        if (dto == null || existsCartItemById(dto.getCartItemId())) {
            return "该购物车记录已存在或传入参数不存在";
        }
        Integer quantity = dto.getQuantity();
        Product product = productService.getById(dto.getProductId());
        if (quantity == null || quantity <= 0) {
            return "购物数量必须大于0";
        }
        if (product == null) {
            return "商品不存在";
        }
        if (product.getStock() < quantity) {
            return "库存数量不足";
        }
        CartItem cartItem = new CartItem();
        BeanUtils.copyProperties(dto, cartItem);
        this.save(cartItem);
        productService.decreaseProductStock(dto.getProductId(), quantity);
        return null;
    }

    @Override
    @Transactional
    public String deleteCartItemById(Integer cartItemId) {
        CartItem cartItem = this.getById(cartItemId);
        if (cartItem == null) {
            return "购物车记录不存在";
        }
        productService.increaseProductStock(cartItem.getProductId(), cartItem.getQuantity());
        this.removeById(cartItemId);
        return null;
    }

    @Override
    @Transactional
    public String updateCartItemById(CartItemDTO dto) {
        if (dto == null || !existsCartItemById(dto.getCartItemId())) {
            return "购物车记录不存在";
        }
        Product product = productService.getById(dto.getProductId());
        if (dto.getQuantity() > product.getStock()) {
            return "库存数量不足";
        }
        CartItem cartItem = new CartItem();
        BeanUtils.copyProperties(dto, cartItem);
        this.updateById(cartItem);
        return null;
    }

    @Transactional
    @Override
    public String increaseCartItemQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = this.getById(cartItemId);
        if (cartItem == null) {
            return "购物车记录不存在";
        }
        if (quantity <= 0) {
            return "修改数量必须大于0";
        }
        Product product = productService.getById(cartItem.getProductId());
        if (quantity > product.getStock()) {
            return "库存数量不足";
        }
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        productService.decreaseProductStock(cartItem.getProductId(), quantity);
        this.updateById(cartItem);
        return null;
    }

    @Transactional
    @Override
    public String decreaseCartItemQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = this.getById(cartItemId);
        if (cartItem == null) {
            return "购物车记录不存在";
        }
        if (quantity <= 0) {
            return "修改数量必须大于0";
        }
        cartItem.setQuantity(cartItem.getQuantity() - quantity);
        if (cartItem.getQuantity() <= 0) {
            deleteCartItemById(cartItemId);
            return "数量小于或等于0，则删除购物车记录";
        }
        productService.increaseProductStock(cartItem.getProductId(), quantity);
        this.updateById(cartItem);
        return null;
    }


    public boolean existsCartItemById(Integer cartItemId) {
        return this.baseMapper.exists(Wrappers.<CartItem>lambdaQuery().eq(CartItem::getCartItemId, cartItemId));
    }
}
