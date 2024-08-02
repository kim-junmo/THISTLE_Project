package com.docmall.thistle.admin.adminorder;

import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.order.OrderVO;
import com.docmall.thistle.payinfo.PayInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminOrderService {

    private final AdminOrderMapper adminOrderMapper;
    private final PayInfoMapper payInfoMapper;

    //주문 목록
    public List<OrderVO> order_list(Criteria cri, String start_date, String end_date) {
        return adminOrderMapper.order_list(cri, start_date, end_date);
    }
    //주문 목록2
    public List<Map<String, Object>> order_list2() {
        return adminOrderMapper.order_list2();
    }

    public int getTotalCount(Criteria cri, String start_date, String end_date) {
        return adminOrderMapper.getTotalCount(cri, start_date, end_date);
    }

    //1)주문자 정보
    public OrderVO order_info(Long ord_code) {
        return adminOrderMapper.order_info(ord_code);
    }

    //2)주문상품 정보
    public List<OrderDetailinfoVO> order_detail_info(Long ord_code) {
        return adminOrderMapper.order_detail_info(ord_code);
    }

    //주문상품 개별 삭제
    @Transactional
    public void order_product_delete(Long ord_code, int pro_num) {

        //주문상품 개별삭제
        adminOrderMapper.order_product_delete(ord_code, pro_num);

        //주문테이블 주문금액 변동
        adminOrderMapper.order_tot_price_change(ord_code);

        //결제테이블 주문금액 변동
        payInfoMapper.pay_tot_price_change(ord_code);
    }

    public void order_basic_modify(OrderVO vo) {
        adminOrderMapper.order_basic_modify(vo);
    }

    //상품 삭제
    public void order_list_delete(Long ord_code) {
        adminOrderMapper.order_list_delete(ord_code);
    }
}
