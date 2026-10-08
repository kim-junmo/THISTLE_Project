package com.docmall.thistle.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 로그인이 필요한 사용자 페이지(장바구니, 주문, 마이쇼핑 등)를 보호하는 인터셉터.
 *
 * 기존에는 컨트롤러마다 session.getAttribute("login_status")를 바로 형변환해서 사용했기 때문에
 * 로그인하지 않은 상태로 접근하면 NullPointerException(500 에러)이 발생했다.
 * 컨트롤러에 들어가기 전에 여기서 먼저 로그인 여부를 확인한다.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("login_status") != null) {
            return true; // 로그인 상태면 그대로 진행
        }

        // ajax 요청이면 화면 이동 대신 401 상태코드만 보낸다. (jQuery는 X-Requested-With 헤더를 붙인다)
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        response.sendRedirect("/user/login");
        return false;
    }
}
