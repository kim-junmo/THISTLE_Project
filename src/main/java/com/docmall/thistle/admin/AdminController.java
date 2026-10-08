package com.docmall.thistle.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /*
     관리자 등록은 아무나 하면 안 된다.
     - 로그인한 관리자가 다른 관리자를 추가하는 경우
     - 관리자가 한 명도 없을 때(최초 관리자 생성)
     두 경우에만 허용한다. (WebMvcConfig에서 /admin/join은 인터셉터에서 제외하고 여기서 직접 확인)
     */
    private boolean canJoin(HttpSession session) {
        return session.getAttribute("admin_state") != null || adminService.adminCount() == 0;
    }

    @GetMapping("/join")
    public String joinForm(HttpSession session) {
        if (!canJoin(session)) {
            return "redirect:/admin/";
        }
        return "admin/join";
    }

    @PostMapping("/join")
    public String joinOK(AdminVO vo, HttpSession session, RedirectAttributes rttr) {
        if (!canJoin(session)) {
            return "redirect:/admin/";
        }

        // 화면에서 검사했더라도 서버에서 한 번 더 중복 확인
        if (adminService.idcheck(vo.getAdmin_id()) != null) {
            rttr.addFlashAttribute("msg", "duplicateID");
            return "redirect:/admin/join";
        }

        vo.setAdmin_pw(passwordEncoder.encode(vo.getAdmin_pw()));

        log.info("관리자 등록 : " + vo.getAdmin_id());

        adminService.adminjoin(vo);

        // 로그인한 관리자가 추가한 경우는 대시보드로, 최초 등록이면 로그인 화면으로
        if (session.getAttribute("admin_state") != null) {
            rttr.addFlashAttribute("msg", "joinSuccess");
            return "redirect:/admin/admin_menu";
        }
        return "redirect:/admin/";
    }

    @GetMapping("idcheck")
    public ResponseEntity<String> idcheckOK(String admin_id, HttpSession session) throws Exception{
        if (!canJoin(session)) {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }

        String idcheck = "";
        if(adminService.idcheck(admin_id) != null) {
            idcheck = "no";
        }else {
            idcheck = "yes";
        }

        return new ResponseEntity<String>(idcheck, HttpStatus.OK);
    }

    @GetMapping("")
    public String loginForm(Model model) {

        // 관리자가 아직 없으면 로그인 화면에 '최초 관리자 등록' 링크를 보여준다
        model.addAttribute("first_admin", adminService.adminCount() == 0);

        return "admin/adlogin";
    }

    @PostMapping("/admin_ok")
    public String adminOk(AdminLoginDTO dto, HttpSession session, RedirectAttributes rttr) throws Exception{
        log.info("관리자 로그인 시도 : " + dto.getAdmin_id());

        AdminVO d_vo = adminService.loginOK(dto.getAdmin_id());

        String url = "";
        String msg = "";

        if (d_vo != null) {
            if (passwordEncoder.matches(dto.getAdmin_pw(), d_vo.getAdmin_pw())) {
                d_vo.setAdmin_pw("");
                session.setAttribute("admin_state", d_vo);
                adminService.updateVisitDate(d_vo.getAdmin_id()); // 최근 접속일 기록
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

    // 관리자 대시보드
    @GetMapping("/admin_menu")
    public void admin_Menu(Model model) {

        model.addAttribute("summary", adminService.getDashboardSummary());
        model.addAttribute("recent_orders", adminService.getRecentOrders());
        model.addAttribute("low_stock_list", adminService.getLowStockProducts());
    }

    // header.html의 로그아웃은 <a href> 링크(GET 요청)이므로 GET으로 매핑
    @GetMapping("/admin_logout")
    public String admin_logoutOK(HttpSession session) {
        session.removeAttribute("admin_state");

        return "redirect:/admin/";
    }

}
