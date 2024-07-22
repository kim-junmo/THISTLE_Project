package com.docmall.thistle.naverlogin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NaverToken {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;
    private String error;
    private String error_description;
}
