package com.docmall.thistle.kakaologin;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class KakaoUserInfo implements Serializable {

    // 세션을 Redis에 저장하려면 세션에 담는 객체가 직렬화 가능해야 한다
    private static final long serialVersionUID = 1L;


    private Long id;
    private String nickname;
    private String email;
}
