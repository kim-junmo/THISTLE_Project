package com.docmall.thistle.naverlogin;

import com.docmall.thistle.user.SNSUserDTO;
import com.docmall.thistle.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Slf4j
@RequestMapping("/oauth2/*")
@RequiredArgsConstructor
@Controller
// 네이버 로그인 기능을 위한 컨트롤러 클래스입니다.
public class NaverLoginController {

    // NaverLoginService와 UserService 객체를 선언합니다.
    private final NaverLoginService naverLoginService;
    private final UserService userService;

    // step1: 네이버 로그인 연동 URL을 생성하는 메서드입니다.
    @GetMapping("/naverlogin") // "/naverlogin" 경로에 대한 GET 요청을 처리합니다.
    public String connect() throws Exception {

        // 네이버 인증 URL을 가져옵니다.
        String url = naverLoginService.getNaverAuthorizerUrl();

        // 네이버 인증 URL로 리다이렉트합니다.
        return "redirect:" + url.toString();
    }

    // step2: 네이버 로그인 콜백을 처리하는 메서드입니다.
    // 콜백 주소는 http://localhost:9090/oauth2/callback/naver입니다.
    @GetMapping("/callback/naver") // "/callback/naver" 경로에 대한 GET 요청을 처리합니다.
    public String callback(NaverCallback callback, HttpSession session) throws UnsupportedEncodingException, Exception {

        // 콜백에서 에러가 있는지 확인하고, 에러가 있으면 로그에 출력합니다.
        if(callback.getError() != null) {
            log.info(callback.getError_description());
        }

        // 네이버 토큰 URL을 가져옵니다.
        String responseToken = naverLoginService.getNaverTokenUrl(callback);

        // ObjectMapper를 사용하여 응답 토큰을 NaverToken 객체로 변환합니다.
        ObjectMapper objectMapper = new ObjectMapper();
        NaverToken naverToken  = objectMapper.readValue(responseToken, NaverToken.class);

        // 토큰 정보를 로그에 출력합니다.
        log.info("토큰정보 : " + naverToken.toString());

        // 토큰을 사용하여 네이버 사용자 정보를 가져옵니다.
        String responseUser = naverLoginService.getNaverUserByToken(naverToken);
        NaverResponse naverResponse = objectMapper.readValue(responseUser, NaverResponse.class);

        // 사용자 정보를 로그에 출력합니다.
        log.info(" 사용자 정보 : " + naverResponse.toString());

        // 네이버 사용자 이메일을 가져옵니다.
        String sns_email = naverResponse.getResponse().getEmail();

        // 네이버 응답이 있으면 세션에 사용자 정보와 액세스 토큰을 저장합니다.
        if(naverResponse != null) {
            session.setAttribute("naver_status", naverResponse);
            session.setAttribute("accessToken", naverToken.getAccess_token());

            // 사용자가 존재하지 않으면 새로운 SNS 사용자 정보를 설정합니다.
            if(userService.existUserInfo(sns_email) == null && userService.sns_user_check(sns_email) == null) {
                SNSUserDTO dto = new SNSUserDTO();

                dto.setId(naverResponse.getResponse().getId());
                dto.setEmail(naverResponse.getResponse().getEmail());
                dto.setName(naverResponse.getResponse().getName());
                dto.setSns_type("naver");
            }
        }
        // 홈 페이지로 리다이렉트합니다.
        return "redirect:/";
    }

    // 네이버 로그아웃을 처리하는 메서드입니다.
    @GetMapping("/naverlogout") // "/naverlogout" 경로에 대한 GET 요청을 처리합니다.
    public String naverlogoutOK(HttpSession session) {

        // 세션에서 액세스 토큰을 가져옵니다.
        String accessToken = (String) session.getAttribute("accessToken");

        // 네이버 토큰을 삭제합니다.
        naverLoginService.getNaverTokenDelete(accessToken);

        // 세션에서 사용자 정보와 액세스 토큰을 제거합니다.
        if(accessToken != null && !"".equals(accessToken)) {
            session.removeAttribute("naver_status");
            session.removeAttribute("accessToken");
        }

        // 홈 페이지로 리다이렉트합니다.
        return "redirect:/";
    }
}