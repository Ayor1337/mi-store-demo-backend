package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.admin.dto.CartItemDTO;
import com.example.entity.admin.vo.CartItemVO;
import com.example.entity.admin.vo.CommodityVO;
import com.example.entity.pojo.CartItem;
import com.example.entity.pojo.Commodity;
import com.example.mapper.CartItemMapper;
import com.example.service.CartItemService;
import com.example.service.CommodityService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

    @Resource
    private CommodityService commodityService;

    // 根据购物车ID获取购物车内所有项目
    @Override
    public List<CartItemVO> getCartItemVOsByCartId(Integer cartId) {
        List<CartItemVO> cartItemVOList = new LinkedList<>();

        this.lambdaQuery()
                .eq(CartItem::getCartId, cartId)
                .list()
                .forEach(cartItem -> {
                    CartItemVO cartItemVO = new CartItemVO();
                    CommodityVO commodityById = commodityService.getCommodityById(cartItem.getCommodityId());
                    BeanUtils.copyProperties(cartItem, cartItemVO);
                    BeanUtils.copyProperties(commodityById, cartItemVO);
                    cartItemVO.setSku(commodityById.getFullName());
                    cartItemVO.setTotalPrice(cartItemVO.getPrice().multiply(new BigDecimal(cartItemVO.getQuantity())));
                    cartItemVOList.add(cartItemVO);
                });
        return cartItemVOList;
    }

    // 删除购物车记录
    @Override
    public String deleteCartItemById(Integer cartItemId) {
        CartItem cartItem = this.getById(cartItemId);
        if (cartItem == null) {
            return "购物车记录不存在";
        }
        final String messageFromIncreaseCommodityStock = commodityService.increaseCommodityStock(cartItem.getCommodityId(), cartItem.getQuantity());
        if (messageFromIncreaseCommodityStock != null) {
            return messageFromIncreaseCommodityStock;
        }
        this.removeById(cartItemId);
        return null;
    }

    @Override
    public String batchDeleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return "购物车记录不存在";
        }
        this.removeBatchByIds(ids);
        return null;
    }

    // 通过购物车ID删除购物车内所有项目
    @Override
    public String deleteAllCartItemByCartId(Integer cartId) {
        if (!existsCartItemById(cartId)) {
            return "购物车记录没有记录";
        }
        this.lambdaQuery()
                .eq(CartItem::getCartId, cartId)
                .list()
                .forEach(cartItem -> {
                    commodityService.increaseCommodityStock(cartItem.getCommodityId(), cartItem.getQuantity());
                    this.removeById(cartItem.getCartItemId());
                });
        return null;
    }

    // 通过商品ID删除购物车内所有项目
    @Override
    public String deleteAllCartItemByProductId(Integer productId) {
        commodityService.lambdaQuery()
                .eq(Commodity::getProductId, productId)
                .list()
                .forEach(commodity -> {
                    commodityService.increaseCommodityStock(commodity.getCommodityId(), commodity.getStock());
                    this.lambdaQuery()
                            .eq(CartItem::getCommodityId, commodity.getCommodityId())
                            .list()
                            .forEach(cartItem ->
                                    this.removeById(cartItem.getCartItemId())
                            );
                });
        return null;
    }

    // 添加购物车记录
    @Override
    public String saveCartItem(CartItemDTO dto) {
        if (dto == null || existsCartItemById(dto.getCartItemId())) {
            return "该购物车记录已存在或传入参数不存在";
        }
        Integer quantity = dto.getQuantity();
        Commodity commodity = commodityService.getById(dto.getCommodityId());
        if (quantity == null || quantity <= 0) {
            return "购物数量必须大于0";
        }
        if (quantity > commodity.getStock()) {
            return "库存数量不足";
        }
        CartItem cartItem = new CartItem();
        BeanUtils.copyProperties(dto, cartItem);
        cartItem.setIsChecked(false);
        this.save(cartItem);

        return commodityService.decreaseCommodityStock(dto.getCommodityId(), quantity);
    }

    // 更新购物车记录
    @Override
    public String updateCartItemById(CartItemDTO dto) {
        if (dto == null || !existsCartItemById(dto.getCartItemId())) {
            return "购物车记录不存在";
        }
        Commodity commodity = commodityService.getById(dto.getCommodityId());
        if (dto.getQuantity() > commodity.getStock()) {
            return "库存数量不足";
        }
        CartItem cartItem = new CartItem();
        BeanUtils.copyProperties(dto, cartItem);
        this.updateById(cartItem);
        return null;
    }

    // 增加购物车记录中的数量单位
    @Override
    public String increaseCartItemQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = this.getById(cartItemId);
        if (cartItem == null) {
            return "购物车记录不存在";
        }
        if (quantity <= 0) {
            return "修改数量必须大于0";
        }
        Commodity commodity = commodityService.getById(cartItem.getCommodityId());
        if (quantity > commodity.getStock()) {
            return "库存数量不足";
        }
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        final String messageFromDecreaseCommodityStock = commodityService.decreaseCommodityStock(cartItem.getCommodityId(), quantity);
        if (messageFromDecreaseCommodityStock != null) {
            return messageFromDecreaseCommodityStock;
        }
        this.updateById(cartItem);
        return null;
    }

    // 减少购物车记录中的数量单位
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
        commodityService.increaseCommodityStock(cartItem.getCommodityId(), quantity);
        this.updateById(cartItem);
        return null;
    }

    // 修改购物车记录中的数量单位
    @Override
    public String changeCartItemQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = this.getById(cartItemId);
        if (cartItem == null) {
            return "购物车记录不存在";
        }
        if (quantity <= 0) {
            return "修改数量必须大于0";
        }
        if (cartItem.getQuantity() <= 0) {
            deleteCartItemById(cartItemId);
            return "数量小于或等于0，则删除购物车记录";
        }
        String message = null;
        if (quantity > cartItem.getQuantity()) {
            message = increaseCartItemQuantity(cartItemId, quantity - cartItem.getQuantity());
        }
        if (quantity < cartItem.getQuantity()) {
            message = decreaseCartItemQuantity(cartItemId, cartItem.getQuantity() - quantity);
        }
        cartItem.setQuantity(quantity);
        this.updateById(cartItem);
        return message;
    }

    // 判断购物车记录是否存在
    public boolean existsCartItemById(Integer cartItemId) {
        return this.baseMapper.exists(Wrappers.<CartItem>lambdaQuery().eq(CartItem::getCartItemId, cartItemId));
    }
}
