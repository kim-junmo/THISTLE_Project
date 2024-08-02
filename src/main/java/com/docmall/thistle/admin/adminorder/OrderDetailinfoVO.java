package com.docmall.thistle.admin.adminorder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class OrderDetailinfoVO {

    private Long ord_code;
    private Date ord_regdate;
    private int pro_num;
    private int dt_amount;
    private int dt_price;
    private String pro_name;
    private int pro_price;
    private String pro_up_folder;
    private String pro_img;


}
