package com.docmall.thistle.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 관리자 페이지(/admin/**)를 보호하는 인터셉터.
 *
 * 기존에는 관리자 로그인을 하지 않아도 /admin/user/user_list 같은 주소를 직접 입력하면
 * 회원 목록 조회, 회원 삭제, 단체 메일 발송까지 누구나 할 수 있었다.
 */
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("admin_state") != null) {
            return true;
        }

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        response.sendRedirect("/admin/");
        return false;
    }
}
