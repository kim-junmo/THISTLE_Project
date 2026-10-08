package com.docmall.thistle.payinfo;

import org.apache.ibatis.annotations.Param;

public interface PayInfoMapper {

    //결제 테이블(INSERT) 작업을 하기 위해 payinfo mapper작업을 함.
    void payinfo_insert(PayInfoVO vo);

    //관리자 주문목록 상세보기를 위해 만들어졌다.
    PayInfoVO ord_pay_info(Long ord_code);

    void pay_tot_price_change(Long ord_code);

    //관리자 주문 진행 상태 변경 (미납 → 결제완료 → 배송준비중 → 배송중 → 배송완료 / 주문취소)
    int p_status_change(@Param("ord_code") Long ord_code, @Param("p_status") String p_status);
}
