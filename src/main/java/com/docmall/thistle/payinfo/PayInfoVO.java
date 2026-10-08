package com.docmall.thistle.payinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

// MyBatis가 조회 결과를 담으려면 기본 생성자가 필요하다. @Builder만 있으면 기본 생성자가 없어진다.
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PayInfoVO {
    //p_id, ord_code, user_id, paymethod, payinfo, p_price, p_status, p_date

    private Integer p_id;
    private Long ord_code;
    private String user_id;
    private String paymethod;
    private String payinfo;
    private int p_price;
    private String p_status;
    private Date p_date;
}
