package com.docmall.thistle.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/admin/*")
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String loginForm() {

        return "admin/adlogin";
    }

    @PostMapping("/admin_ok")
    public String adminOk(AdminVO vo, HttpSession session) throws Exception{
        log.info("관리자 정보 : " + vo);

        AdminVO d_vo = adminService.loginOK(vo.getAdmin_id());

        String url = "";

        if (d_vo != null) {
            if (passwordEncoder.matches(vo.getAdmin_pw(), d_vo.getAdmin_pw())) {

                d_vo.setAdmin_pw("");
                session.setAttribute("admin_state", d_vo);
                url = "admin/admin_menu";
            }
        }

        return "redirect:/" + url;
    }
    @GetMapping("/admin_menu")
    public void admin_Menu() {

    }
    @PostMapping("/admin_logout")
    public String admin_logoutOK(HttpSession session) {
        session.removeAttribute("admin_state");

        return "redirect:/admin/";
    }
}
