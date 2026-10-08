package com.docmall.thistle.common.config;

import com.docmall.thistle.common.interceptor.AdminInterceptor;
import com.docmall.thistle.common.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 1) 사용자 로그인이 필요한 주소
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(
                        "/cart/**",
                        "/order/**",
                        "/myshop/**",
                        "/kakao/kakaopay",
                        "/kakao/approval",
                        "/user/modify",
                        "/user/changepw",
                        "/user/delete",
                        "/review/review_save",
                        "/review/review_modify/**",
                        "/review/review_modify",
                        "/review/review_delete/**"
                );
        // /user/mypage는 카카오 로그인 사용자(kakao_status)도 볼 수 있도록 컨트롤러에서 직접 처리하므로 제외

        // 2) 관리자 로그인이 필요한 주소
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin",
                        "/admin/",              // 관리자 로그인 화면
                        "/admin/admin_ok",      // 관리자 로그인 처리
                        "/admin/adlogin",
                        // ckeditor로 올린 이미지: 상품 상세설명과 발송된 메일 안에서 일반 사용자도 봐야 한다
                        "/admin/product/display/**",
                        "/admin/user/display/**"
                );
    }
}
