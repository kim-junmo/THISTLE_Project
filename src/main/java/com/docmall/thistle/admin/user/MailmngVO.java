package com.docmall.thistle.admin.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class MailmngVO {

    //m_idx, m_title, m_content, m_gubun, m_send_count, reg_date
    //resultMap을 사용하여 작업을 할 것이기 때문에 sql 쿼리와 이름이 다르다.

    private Integer idx;
    private String title;
    private String content;
    private String gubun;
    private int sendcount;
    private Date regDate;

}
