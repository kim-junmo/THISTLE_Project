package com.docmall.thistle.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
