package com.docmall.thistle.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 세션 저장소를 톰캣 메모리 → Redis로 바꾸는 설정.
 *
 * 코드에서는 지금처럼 HttpSession을 그대로 사용하면 되고(session.setAttribute 등),
 * 실제 저장 위치만 Redis로 바뀐다.
 *  - 서버를 재시작해도 로그인이 풀리지 않는다.
 *  - 서버를 여러 대로 늘려도 같은 세션을 공유한다.
 *
 * 브라우저에는 세션 ID만 쿠키(THISTLE_SESSION)로 저장되고, 사용자 정보는 Redis에만 있다.
 * Redis 접속 정보는 기본값 localhost:6379 이며,
 * 바꾸려면 application.properties에 spring.data.redis.host / spring.data.redis.port 를 넣는다.
 */
@Configuration
@EnableRedisHttpSession(
        maxInactiveIntervalInSeconds = 1800,      // 30분 동안 요청이 없으면 세션 만료 (톰캣 기본값과 동일)
        redisNamespace = "thistle:session"        // Redis 안에서 이 프로젝트 세션 키의 접두어
)
public class RedisSessionConfig {

    // 세션 ID를 담는 쿠키 설정
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("THISTLE_SESSION");
        serializer.setCookiePath("/");
        serializer.setUseHttpOnlyCookie(true);  // 자바스크립트에서 세션 쿠키를 읽지 못하게 (XSS로 세션 탈취 방지)
        // Lax: 다른 사이트에서 넘어오는 일반 링크/리다이렉트(GET)에는 쿠키가 붙는다.
        // 카카오·네이버 로그인, 카카오페이 승인 후 돌아올 때 로그인 상태가 유지되려면 Strict가 아닌 Lax여야 한다.
        serializer.setSameSite("Lax");
        return serializer;
    }
}
