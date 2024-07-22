package com.docmall.thistle.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class AdminVO {

    private String admin_id;
    private String admin_pw;
    private Date admin_visit_date;
}
