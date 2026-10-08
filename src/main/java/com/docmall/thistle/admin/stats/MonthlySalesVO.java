package com.docmall.thistle.admin.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 월별 매출 한 줄 (1월 ~ 12월)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySalesVO {

    private int month;          // 1 ~ 12
    private int order_count;    // 주문 건수
    private long sales;         // 매출 합계
}
