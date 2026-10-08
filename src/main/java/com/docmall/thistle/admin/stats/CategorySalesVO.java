package com.docmall.thistle.admin.stats;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 1차 카테고리별 매출 한 줄
@Getter
@Setter
@ToString
public class CategorySalesVO {

    private Integer cate_code;
    private String cate_name;
    private int quantity;       // 판매 수량
    private long sales;         // 매출 합계 (수량 * 판매가)
}
