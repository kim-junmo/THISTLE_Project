package com.docmall.thistle.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequestMapping("/admin/*")
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public void joinForm() {

    }

    @PostMapping("/join")
    public String joinOK(AdminVO vo) {
        vo.setAdmin_pw(passwordEncoder.encode(vo.getAdmin_pw()));

        log.info("관리자 정보" + vo);

        adminService.adminjoin(vo);

        return "redirect:/admin/adlogin";
    }

    @GetMapping("idcheck")
    public ResponseEntity<String> idcheckOK(String admin_id) throws Exception{
        ResponseEntity<String> entity = null;

        String idcheck = "";
        if(adminService.idcheck(admin_id) != null) {
            idcheck = "no";
        }else {
            idcheck = "yes";
        }

        entity = new ResponseEntity<String>(idcheck, HttpStatus.OK);

        return entity;
    }

    @GetMapping("")
    public String loginForm() {

        return "admin/adlogin";
    }

    @PostMapping("/admin_ok")
    public String adminOk(AdminLoginDTO dto, HttpSession session, RedirectAttributes rttr) throws Exception{
        log.info("관리자 정보 : " + dto);

        AdminVO d_vo = adminService.loginOK(dto.getAdmin_id());

        String url = "";
        String msg = "";

        if (d_vo != null) {
            if (passwordEncoder.matches(dto.getAdmin_pw(), d_vo.getAdmin_pw())) {
                d_vo.setAdmin_pw("");
                session.setAttribute("admin_state", d_vo);
                url = "/admin/admin_menu";
            }else {
                msg = "failPW";
                url = "/admin/";
            }
        }else {
            msg = "failID";
            url = "/admin/";
        }


        rttr.addFlashAttribute("msg", msg);
        // 로그인 실패 시에도 관리자 메뉴로 이동하던 문제 수정: 결과에 따라 이동
        return "redirect:" + url;
    }
    @GetMapping("/admin_menu")

    public void admin_Menu() {

    }
    // header.html의 로그아웃은 <a href> 링크(GET 요청)이므로 GET으로 매핑
    @GetMapping("/admin_logout")
    public String admin_logoutOK(HttpSession session) {
        session.removeAttribute("admin_state");

        return "redirect:/admin/";
    }

}
