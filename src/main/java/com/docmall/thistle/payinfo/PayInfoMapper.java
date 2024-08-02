package com.docmall.thistle.payinfo;

public interface PayInfoMapper {

    //결제 테이블(INSERT) 작업을 하기 위해 payinfo mapper작업을 함.
    void payinfo_insert(PayInfoVO vo);

    //관리자 주문목록 상세보기를 위해 만들어졌다.
    PayInfoVO ord_pay_info(Long ord_code);

    void pay_tot_price_change(Long ord_code);
}
