package com.docmall.thistle.naverlogin;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Response implements Serializable {

    // 세션을 Redis에 저장하려면 세션에 담는 객체가 직렬화 가능해야 한다
    private static final long serialVersionUID = 1L;


    private String id;
    private String nickname;
    private String name;
    private String email;
    private String gender;
    private String age;
    private String birthday;
    private String profile_image;
    private String birthyear;
    private String mobile;
}
