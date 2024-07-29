package com.docmall.thistle.order;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

    //카카오페이 주문 테이블 작업
    void order_insert(OrderVO vo);

    //카카오페이 주문 디테일 추가를 위해 작업
    void orderDetail_insert(@Param("ord_code") Long ord_code, @Param("user_id") String user_id);
}
