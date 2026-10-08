package com.docmall.thistle.naverlogin;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverResponse implements Serializable {

    // 세션을 Redis에 저장하려면 세션에 담는 객체가 직렬화 가능해야 한다
    private static final long serialVersionUID = 1L;


    private String resultcode;
    private String message;

    Response response;
}
