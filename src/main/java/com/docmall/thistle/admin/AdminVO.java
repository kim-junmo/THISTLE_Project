package com.docmall.thistle.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class AdminVO {

    //admin_id, admin_pw, admin_dept, admin_email, admin_phone, admin_datesub, admin_updatedate, admin_visit_date

    private String admin_id;
    private String admin_pw;
    private String admin_dept;
    private String admin_email;
    private String admin_phone;
    private Date admin_datesub;
    private Date admin_updatedate;
    private Date admin_visit_date;
}
