package com.docmall.thistle.naverlogin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@Service
public class NaverLoginService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.redirect.uri}")
    private String redirectUri;

    @Value("${naver.client.secret}")
    private String clientSecret;

    // 네이버 인증 URL을 생성하여 반환합니다.
    public String getNaverAuthorizerUrl() throws UnsupportedEncodingException {

        // UriComponentsBuilder를 사용하여 URI를 구성합니다.
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/authorize") // 기본 URL을 설정합니다.
                .queryParam("response_type", "code") // 쿼리 파라미터를 추가합니다.
                .queryParam("client_id", clientId)
                .queryParam("state", URLEncoder.encode("1234", "UTF-8"))
                .queryParam("redirect_uri", URLEncoder.encode(redirectUri, "UTF-8"))
                .build(); // URI를 빌드합니다.

        // 구성된 URI를 문자열로 반환합니다.
        return uriComponents.toString();
    }

    // 네이버 토큰 URL을 생성하고, 토큰을 요청하여 반환합니다.
    public String getNaverTokenUrl(NaverCallback callback) {

        try {
            // UriComponentsBuilder를 사용하여 URI를 구성합니다.
            UriComponents uriComponents = UriComponentsBuilder
                    .fromUriString("https://nid.naver.com/oauth2.0/token") // 기본 URL을 설정합니다.
                    .queryParam("grant_type", "authorization_code") // 쿼리 파라미터를 추가합니다.
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("code", callback.getCode())
                    .queryParam("state", URLEncoder.encode(callback.getState(), "UTF-8"))
                    .build(); // URI를 빌드합니다.
            URL url = new URL(uriComponents.toString()); // URL 객체를 생성합니다.

            // HTTP 연결을 설정합니다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // HTTP GET 요청을 설정합니다.

            int responseCode = conn.getResponseCode(); // 응답 코드를 받습니다.
            BufferedReader br;

            // 응답 코드에 따라 적절한 입력 스트림을 설정합니다.
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) { // 응답을 한 줄씩 읽어들입니다.
                response.append(inputLine);
            }
            br.close(); // BufferedReader를 닫습니다.

            // 응답 데이터를 로그에 출력합니다.
            log.info("응답데이터 : " + response.toString());
            return response.toString(); // 응답 데이터를 반환합니다.

        } catch (Exception e) {
            e.printStackTrace(); // 예외가 발생하면 스택 트레이스를 출력합니다.
        }
        return null; // 예외가 발생하면 null을 반환합니다.
    }

    // 토큰을 사용하여 네이버 사용자 정보를 요청하고 반환합니다.
    public String getNaverUserByToken(NaverToken naverToken) {
        String accessToken = naverToken.getAccess_token();
        String tokenType = naverToken.getToken_type();

        try {
            URL url = new URL("https://openapi.naver.com/v1/nid/me"); // 사용자 정보 요청 URL을 설정합니다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // HTTP GET 요청을 설정합니다.
            conn.setRequestProperty("Authorization", tokenType + " " + accessToken); // 요청 헤더에 인증 정보를 추가합니다.

            int responseCode = conn.getResponseCode(); // 응답 코드를 받습니다.
            BufferedReader br;

            // 응답 코드에 따라 적절한 입력 스트림을 설정합니다.
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) { // 응답을 한 줄씩 읽어들입니다.
                response.append(inputLine);
            }

            br.close(); // BufferedReader를 닫습니다.

            // 사용자 정보 응답 데이터를 로그에 출력합니다.
            log.info("사용자 정보 응답 데이터 : " + response.toString());

            return response.toString(); // 사용자 정보 응답 데이터를 반환합니다.
        } catch (Exception e) {
            e.printStackTrace(); // 예외가 발생하면 스택 트레이스를 출력합니다.
        }
        return null; // 예외가 발생하면 null을 반환합니다.
    }

    // 네이버 토큰을 삭제합니다.
    public void getNaverTokenDelete(String access_token) {

        try {
            // UriComponentsBuilder를 사용하여 URI를 구성합니다.
            UriComponents uriComponents = UriComponentsBuilder
                    .fromUriString("https://nid.naver.com/oauth2.0/token") // 기본 URL을 설정합니다.
                    .queryParam("grant_type", "delete") // 쿼리 파라미터를 추가합니다.
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("access_token", URLEncoder.encode(access_token, "UTF-8"))
                    .build(); // URI를 빌드합니다.
            URL url = new URL(uriComponents.toString()); // URL 객체를 생성합니다.

            // HTTP 연결을 설정합니다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // HTTP GET 요청을 설정합니다.

            int responseCode = conn.getResponseCode(); // 응답 코드를 받습니다.
        } catch (Exception e) {
            e.printStackTrace(); // 예외가 발생하면 스택 트레이스를 출력합니다.
        }
    }
}

