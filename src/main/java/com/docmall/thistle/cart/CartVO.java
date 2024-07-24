package com.docmall.thistle.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CartVO {

    //cart_code, pro_num, user_id, cart_amount, cart_date

    private long cart_code;
    private int pro_num;
    private String user_id;
    private int cart_amount;
    private Date cart_date;
}
