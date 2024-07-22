package com.docmall.thistle.admin.product;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/*
product_tbl
pro_num. cate_code, pro_name, pro_price, pro_discount, pro_publisher, pro_content, pro_up_folder, pro_img, pro_amount, pro_buy, pro_date, pro_updatedate
 */
@Getter
@Setter
@ToString
public class ProductVO {

    private Integer pro_num;
    private Integer cate_code;
    private String pro_name;
    private int pro_price;
    private int pro_discount;
    private String pro_publisher;
    private String pro_content;
    private String pro_up_folder;
    private String pro_img;
    private Integer pro_amount;
    private String pro_buy;
    private Date pro_date;
    private Date pro_updatedate;
}
