package com.docmall.thistle.kakaologin;

public interface KakaoLoginMapper {

    KakaoUserInfo existsKakaoInfo(String sns_email);

    void kakao_insert(KakaoUserInfo kakaoUserInfo);
}
