package com.docmall.thistle.kakaologin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor

public class KakaoLoginService {

    private final KakaoLoginMapper kakaoLoginMapper;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.oauth.tokenuri}")
    private String tokenUri;

    @Value("${kakao.oauth.userinfouri}")
    private String userinfoUri;

    @Value("${kakao.user.logout}")
    private String kakaologout;

    /* 액세스 토큰을 받기위한 정보
    주소: https://kauth.kakao.com/oauth/token 주소호출
	요청방식: post
	헤더(Header) : Content-type: application/x-www-form-urlencoded;charset=utf-8
	본문(Body) :
	grant_type : authorization_code
	client_id : 앱 REST API 키
	redirect_uri : 인가 코드가 리다이렉트된 URI
	code : 인가 코드 받기 요청으로 얻은 인가 코드
	client_secret : 토큰 발급 시, 보안을 강화하기 위해 추가 확인하는 코드 */

    public String getAccessToken(String code) throws Exception {
        // 메서드 선언: 공개(public) 메서드, String 반환, 예외 처리 위임
        // 기능: 카카오 OAuth 서버로부터 액세스 토큰을 받아오는 메서드

        HttpHeaders headers = new HttpHeaders();
        // HttpHeaders 객체 생성: HTTP 요청 헤더 설정을 위한 객체
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        // 헤더 추가: Content-Type 설정, form-urlencoded 형식 및 UTF-8 인코딩 지정

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        // MultiValueMap 인터페이스를 구현한 LinkedMultiValueMap 객체 생성
        // HTTP 요청 바디 파라미터를 담기 위한 맵 구조
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);
        // 맵에 OAuth2.0 필수 파라미터 추가

        HttpEntity<MultiValueMap<String, String>> tokenKakaoRequest = new HttpEntity<>(body, headers);
        // HttpEntity 객체 생성: HTTP 요청의 헤더와 바디를 포함하는 객체
        // 제네릭을 사용하여 바디의 타입을 명시

        RestTemplate restTemplate = new RestTemplate();
        // RestTemplate 객체 생성: HTTP 요청을 보내기 위한 스프링 프레임워크 클래스

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                tokenUri, HttpMethod.POST, tokenKakaoRequest, String.class
        );
        // HTTP POST 요청 실행 및 응답 수신
        // exchange 메서드: URL, HTTP 메서드, 요청 엔티티, 응답 바디 타입을 인자로 받음

        String responseBody = responseEntity.getBody();
        // 응답 엔티티에서 바디(내용) 추출

        log.info("응답데이터: " + responseBody);
        // 로깅: 문자열 연결 연산자(+)를 사용하여 로그 메시지 생성

        ObjectMapper objectMapper = new ObjectMapper();
        // ObjectMapper 객체 생성: JSON 파싱을 위한 Jackson 라이브러리 클래스
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        // JSON 문자열을 JsonNode 객체로 파싱

        return jsonNode.get("access_token").asText();
        // JSON에서 "accessToken" 필드의 값을 문자열로 추출하여 반환
    }

    public KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 메서드 선언: 사용자 정의 타입 KakaoUserInfo를 반환하는 공개 메서드
        // 기능: 액세스 토큰을 사용하여 카카오 서버로부터 사용자 정보를 조회

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP 요청 헤더 설정: Authorization Bearer 토큰 및 Content-Type 지정

        HttpEntity<MultiValueMap<String, String>> userinfokakaoRequest = new HttpEntity<>(headers);
        // HttpEntity 객체 생성: 헤더만 포함, 바디는 없음

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                userinfoUri, HttpMethod.POST, userinfokakaoRequest, String.class
        );
        // 카카오 사용자 정보 API에 POST 요청 실행

        String responseBody = responseEntity.getBody();
        log.info("응답 사용자 정보 : " + responseBody);
        // 응답 내용 추출 및 로깅

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        // JSON 응답 파싱

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        // JsonNode에서 필요한 정보 추출
        // 메서드 체이닝을 통해 중첩된 JSON 객체에서 값 추출

        return new KakaoUserInfo(id, nickname, email);
        // KakaoUserInfo 객체 생성 및 반환
    }

    public void kakaologout(String accessToken) throws JsonProcessingException {
        // 메서드 선언: 반환값이 없는(void) 공개 메서드
        // 기능: 카카오 로그아웃 요청 실행

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded");
        // 로그아웃 요청을 위한 HTTP 헤더 설정

        HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
        // 헤더만 포함한 HttpEntity 객체 생성

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                kakaologout, HttpMethod.POST, kakaoLogoutRequest, String.class
        );
        // 카카오 로그아웃 API에 POST 요청 실행

        String responseBody = response.getBody();
        log.info("responseBody : " + responseBody);
        // 응답 내용 추출 및 로깅

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        // JSON 응답 파싱

        Long id = jsonNode.get("id").asLong();
        log.info("id : " + id);
        // 로그아웃된 사용자 ID 추출 및 로깅
    }
}
