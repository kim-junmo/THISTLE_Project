package com.docmall.thistle.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class OrderVO {

//    테이블명 : order_tbl
//    시퀀스 명 : seq_ord_code
//    인덱스명 : pk_ord_code
//    컬럼명 : ord_code, user_id, ord_name, ord_addr_zipcode, ord_addr_basic, ord_addr_detail, ord_tel, ord_price, ord_desc, ord_regdate, ord_admin_memo

    private Long ord_code;
    private String user_id;
    private String ord_name;
    private String ord_addr_zipcode;
    private String ord_addr_basic;
    private String ord_addr_detail;
    private String ord_tel;
    private int ord_price;
    private String ord_desc;
    private Date ord_regdate;
    private String ord_admin_memo;
}
