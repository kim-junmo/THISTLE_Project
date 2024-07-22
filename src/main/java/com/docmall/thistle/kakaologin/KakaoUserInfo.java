package com.docmall.thistle.kakaologin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class KakaoUserInfo {

    private Long id;
    private String nickname;
    private String email;
}
