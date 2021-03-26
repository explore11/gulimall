package com.song.gulimall.gulimallcart.service;

import com.song.gulimall.gulimallcart.vo.CartItemVo;
import com.song.gulimall.gulimallcart.vo.CartVo;

import java.util.List;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-26 17:09
 **/
public interface CartService {
    CartItemVo addCartItem(Long skuId, Integer num);

    CartItemVo getCartItem(Long skuId);

    CartVo getCart();

    void checkCart(Long skuId, Integer isChecked);

    void changeItemCount(Long skuId, Integer num);

    void deleteItem(Long skuId);

    List<CartItemVo> getCheckedItems();

}
