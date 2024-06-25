package com.docmall.thistle.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    public final UserMapper userMapper;

    public void join(UserVO vo) {
        userMapper.join(vo);
    }

    public String idcheck(String user_id) {
        return userMapper.idcheck(user_id);
    }

    public UserVO login(String user_id) {
        return userMapper.login(user_id);
    }

    public String idfind(String user_name, String user_email ) {
        return userMapper.idfind(user_name, user_email);
    }

    public String pwfind(String user_id, String user_name, String user_email) {
        return userMapper.pwfind(user_id, user_name, user_email);
    }
    //임시비밀번호 발급
    public String getTempPw() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

    //비밀번호 재발급
    public void tempPwUpdate(String user_id, String temp_enc_pw) {
        userMapper.tempPwUpdate(user_id, temp_enc_pw);
    }
}
