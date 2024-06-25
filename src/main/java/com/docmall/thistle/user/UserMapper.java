package com.docmall.thistle.user;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    //회원가입
    public void join(UserVO vo);

    //아이디 중복체크
    String idcheck(String user_id);

    //로그인
    UserVO login(String user_id);

    //아이디 찾기
    String idfind(@Param("user_name") String user_name, @Param("user_email") String user_email);

    //비밀번호 찾기
    String pwfind(@Param("user_id") String user_id, @Param("user_name") String user_name, @Param("user_email") String user_email);

    //임시비밀번호
    void tempPwUpdate(@Param("user_id") String user_id, @Param("temp_enc_ow") String temp_enc_ow);
}
