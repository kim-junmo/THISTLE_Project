package com.docmall.thistle.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 관리자 대시보드 상단 요약 카드에 들어갈 숫자들
@Getter
@Setter
@ToString
public class DashboardVO {

    private int today_order_count;   // 오늘 주문 건수
    private long today_sales;        // 오늘 매출
    private long month_sales;        // 이번 달 매출
    private int today_user_count;    // 오늘 가입한 회원 수
    private int unpaid_count;        // 입금 대기(미납) 주문 수
    private int shipping_wait_count; // 배송 준비가 필요한 주문 수(결제완료 + 배송준비중)
    private int product_count;       // 전체 상품 수
}
