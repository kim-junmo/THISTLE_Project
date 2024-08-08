package com.docmall.thistle.admin.user;

import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.user.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminUserMapper {

    List<UserVO> user_list(@Param("cri") Criteria cri, @Param("start_date") String start_date, @Param("end_date") String end_date);

    int getTotalCount(@Param("cri") Criteria cri, @Param("start_date") String start_date, @Param("end_date") String end_date);

    void user_delete(String user_id);

    List<MailmngVO> mailingInfoList(@Param("cri") Criteria cri, @Param("title") String title);

    int MailListCount(String title);

    //1. 보낸 메일내용정보 db에 저장
    void mailing_save(MailmngVO vo);

    //2.1. 회원테이블에서 전체 회원 메일 정보를 읽어오는 작업
    //배열을 사용하는 이유는 회원테이블에 회원 정보는 정해져 있기 때문이다.
    String[] getAllMailAddress();

    //메일 발송 횟수 업데이트
    void mailSendCountUpdate(int idx);

    MailmngVO MailSendInfo(int idx);

    void mailingedit(MailmngVO vo);
}
