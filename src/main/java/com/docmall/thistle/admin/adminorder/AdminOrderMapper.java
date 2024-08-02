package com.docmall.thistle.admin.adminorder;

import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.order.OrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;

public interface AdminOrderMapper {

    //주문 목록1
    List<OrderVO> order_list(@Param("cri") Criteria cri, @Param("start_date") String start_date, @Param("end_date") String end_date);

    //주문 목록2
    List<Map<String, Object>> order_list2();

    int getTotalCount(@Param("cri") Criteria cri, @Param("start_date") String start_date, @Param("end_date") String end_date);
    
    //1)주문자 정보
    OrderVO order_info(Long ord_code);
    
    //2)주문상품 정보
    List<OrderDetailinfoVO> order_detail_info(Long ord_code);

    //상품 개별 삭제
    void order_product_delete(@Param("ord_code") Long ord_code, @Param("pro_num") int pro_num);

    //주문테이블 총 주문금액 변동
    void order_tot_price_change(Long ord_code);

    //주문목록 수정
    void order_basic_modify(OrderVO vo);
    
    //상품 삭제하기
    void order_list_delete(Long ord_code);



}
