package com.docmall.thistle.kakaologin;

import com.docmall.thistle.user.SNSUserDTO;
import com.docmall.thistle.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/oauth2/*")
@Controller
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final UserService userService;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    //카카오 로그인을 위해 카카오 서버로부터 인가 코드 요청
    @GetMapping("/kakaologin")
    public String connect() {

        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("response_type=code");
        url.append("&client_id=" + clientId);
        url.append("&redirect_uri=" + redirectUri); //http://localhost:9090/oauth2/callback/kakao

        url.append("&prompt=login");

        return "redirect:" + url.toString();
    }

    //카카오 로그인 api server에서 callback 주소를 호출. 카카오 개발자 사이트에서 미리 준비해야 함.
    @GetMapping("/callback/kakao")
    public String callback(String code, HttpSession session) {

        log.info("code : " + code);

        String accessToken = "";
        KakaoUserInfo kakaoUserInfo = null;

        try {
            accessToken = kakaoLoginService.getAccessToken(code);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
            try {
                kakaoUserInfo = kakaoLoginService.getKakaoUserInfo(accessToken);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            if(kakaoUserInfo != null) {
                log.info("사용자 정보 : " + kakaoUserInfo);

                session.setAttribute("kakao_status", kakaoUserInfo);
                session.setAttribute("accessToken", accessToken);

                String sns_email = kakaoUserInfo.getEmail();

                String sns_login_type = userService.existUserInfo(sns_email);

                if(userService.existUserInfo(sns_email) == null && userService.sns_user_check(sns_email) == null) {
                    SNSUserDTO dto = new SNSUserDTO();
                    dto.setId(kakaoUserInfo.getId().toString());
                    dto.setEmail(kakaoUserInfo.getEmail());
                    dto.setName(kakaoUserInfo.getNickname());
                    dto.setSns_type("kakao");

                    userService.sns_user_insert(dto);
                }
            }
        return "redirect:/";
    }

    @GetMapping("/kakaologout")
    public String kakaologout(HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");

        if(accessToken != null && !"".equals(accessToken)) {
            try {
                kakaoLoginService.kakaologout(accessToken);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            session.removeAttribute("kakao_status");
            session.removeAttribute("accessToken");
        }
        return "redirect:/";
    }
}
