package com.docmall.thistle.cart;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {

    void cart_add(CartVO vo);

    List<CartProductVO> cart_list(String user_id);

    void cart_change(@Param("cart_code") Long cart_code, @Param("cart_amount") int cart_amount);

    void cart_del(Long cart_code);

    void cart_empty(String user_id);
}
