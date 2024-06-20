package com.docmall.thistle.user;

public interface UserMapper {

    //회원가입
    public void join(UserVO vo);

    //아이디 중복체크
    String idcheck(String user_id);
}
