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

    // 내 정보 수정
    void modify(UserVO vo);

    //비밀번호 변경
    void changepw(@Param("user_id") String user_id, @Param("new_user_password") String new_user_password);

    //회원 탈퇴
    void delete(String user_id);

    //sns 가입
    String existUserInfo(String sns_email);

    //sns 유저 중복체크
    String sns_user_check(String sns_email);

    //sns 데이터 삽입
    void sns_user_insert(SNSUserDTO dto);


}
