package com.docmall.thistle.admin.review;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

// 관리자 리뷰 목록용: 리뷰 + 상품명
@Getter
@Setter
@ToString
public class AdminReviewVO {

    private Long re_code;
    private String user_id;
    private int pro_num;
    private String pro_name;
    private String re_title;
    private String re_content;
    private int re_rate;
    private Date re_date;
}
