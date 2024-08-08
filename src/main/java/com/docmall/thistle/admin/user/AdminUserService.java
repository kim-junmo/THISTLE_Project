package com.docmall.thistle.admin.user;

import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class AdminUserService {

    public final AdminUserMapper adminUserMapper;

    public List<UserVO> user_list(Criteria cri, String start_date,  String end_date) {
        return adminUserMapper.user_list(cri, start_date, end_date);
    }

    public int getTotalCount(Criteria cri, String start_date, String end_date) {
        return adminUserMapper.getTotalCount(cri, start_date, end_date);
    }

    public void user_delete(String user_id) {
        adminUserMapper.user_delete(user_id);
    }

    //메일 발송 목록
    public List<MailmngVO> mailingInfoList(Criteria cri, String title) {
        return adminUserMapper.mailingInfoList(cri, title);
    }

    public int MailListCount(String title) {
        return adminUserMapper.MailListCount(title);
    }

    //1. 보낸 메일내용정보 db에 저장
    public void mailing_save(MailmngVO vo) {
        adminUserMapper.mailing_save(vo);
    }

    //2.1. 회원테이블에서 전체 회원 메일 정보를 읽어오는 작업
    //배열을 사용하는 이유는 회원테이블에 회원 정보는 정해져 있기 때문이다.
    public String[] getAllMailAddress() {
        return adminUserMapper.getAllMailAddress();
    }

    //메일 발송 횟수 업데이트
    public void mailSendCountUpdate(int idx) {
        adminUserMapper.mailSendCountUpdate(idx);
    }

    public MailmngVO MailSendInfo(int idx) {
        return adminUserMapper.MailSendInfo(idx);
    }

    public void mailingedit(MailmngVO vo) {
        adminUserMapper.mailingedit(vo);
    }

}
