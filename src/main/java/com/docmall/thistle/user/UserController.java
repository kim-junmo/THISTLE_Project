package com.docmall.thistle.user;

import com.docmall.thistle.common.mail.EmailDTO;
import com.docmall.thistle.common.mail.EmailService;
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
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
    private final UserService userService;

    //비밀번호 저장 시 보안을 위해 만듬(서버로 비밀번호가 저장될 시 60자로 변경)
    //PasswordEncoder를 사용하기 위해 SecurityConfig에 bean을 설정
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    //패턴 : 저장 OK, 양식 : form, 삭제 : delete
    //회원가입
    @GetMapping("join")
    public void joinForm() {
        log.info("join");
    }

    @PostMapping("join")
    public String joinOK(UserVO vo) throws Exception {

        //비밀번호를 안호화하여 저장함.
        vo.setUser_password(passwordEncoder.encode(vo.getUser_password()));

        log.info("회원정보: + " + vo);

        userService.join(vo);
        return "redirect:/user/login";
    }

    @GetMapping("/idcheck")
    //ResponseEntity<String>를 사용한 이유는 아이디 중복체크는 ajax로 진행이 되기 때문
    //ajax로 작업이 진행되면 ResponseEntity를 사용해야 한다.
    //또한 사용자가 입력하는 아이디는 byte가 정해져있지 않고 유동적이기 때문에 ResponseEntity를 사용함!
    //throws Exception를 사용한 이유는 아이디 중복체크는 서버에서 타 사용자의 아이디와 유사한 아이디가 있는 지 체크해야 되기 때문
    public ResponseEntity<String> idcheck(String user_id) throws Exception {

        //entity를 null로 지정하는 이유는 값을 초기화하기 위해서이다. 초기화 하지 않으면 컴파일 오류가 발생할 수 있기 때문!
        ResponseEntity<String> entity = null;

        //아이디 중복체크 초기화, 변수 선언(iduse)
        String iduse = "";
        //userService.idcheck(user_id): 사용자 아이디가 데이터베이스에 존재하는지 여부 체크
        //데이터베이스에 사용자가 입력한 아이디가 있으면 No
        if(userService.idcheck(user_id) != null) {
            iduse = "NO";
        }else if(userService.idcheck(user_id) == null) {
            iduse = "yes";
        }

        entity = new ResponseEntity<String>(iduse, HttpStatus.OK);

        return entity;
    }

    @GetMapping("login")
    public void loginForm() {
        log.info("login");
    }

    @PostMapping("/login")
    public String loginOK(LoginDTO dto, HttpSession session, RedirectAttributes rttr) throws Exception {

        UserVO vo = userService.login(dto.getUser_id());  // 사용자 ID로 사용자 정보 조회

        String msg = "";  // 리다이렉트 시 전달할 메시지 변수 초기화
        String url = "/";  // 로그인 성공 시 리다이렉트할 기본 URL

        if (vo != null) {  // 사용자 정보가 존재하는지 확인

            if (passwordEncoder.matches(dto.getUser_password(), vo.getUser_password())) {  // 비밀번호가 일치하는지 확인
                vo.setUser_password("");  // 비밀번호를 빈 문자열로 설정 (보안)
                session.setAttribute("login_status", vo);  // 세션에 사용자 정보 저장
            }else {
                msg = "failPW";  // 비밀번호가 일치하지 않으면 메시지 설정
                url = "/user/login";  // 로그인 페이지로 리다이렉트할 URL 설정
            }
        }else {
            msg = "failID";  // 사용자 ID가 존재하지 않으면 메시지 설정
            url = "/user/login";  // 로그인 페이지로 리다이렉트할 URL 설정
        }

        rttr.addFlashAttribute("msg", msg);  // 리다이렉트 시 메시지 전달

        return "redirect:" + url;  // 설정된 URL로 리다이렉트
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) throws Exception {

        session.invalidate(); // invalidate: 세션형태로 관리되는 모든 메모리 소멸.

        return "redirect:/";
    }

    @GetMapping("/idfind")
    public void idfindForm() {

    }

    @PostMapping("/idfind")
    public String idfindOK(String user_name, String user_email, String authcode, HttpSession session, RedirectAttributes rttr) throws Exception {

        String url = "";
        String msg = "";

        if(authcode.equals(session.getAttribute("authcode"))) {

            String user_id = userService.idfind(user_name, user_email);
            if(user_id != null) {
                //subject은 emailDTO 안에 있음.
                String subject = "THISTLE 아이디를 보냅니다.";
                String message = "아이디를 확인하시고, 로그인을 해주세요.";

                EmailDTO dto = new EmailDTO("THISTLE", "THISTLE", user_email, subject, message);

                emailService.sendMail("emailIDResult", dto, user_id);

                session.removeAttribute("authcode");

                msg = "success";
                url = "/user/login";
                rttr.addFlashAttribute("msg", msg);

            }else {
                msg = "failID";
                url = "/user/idfind";
            }


        }else {
            msg = "failAuthCode";
            url = "/user/idfind";
        }
        rttr.addFlashAttribute("msg", msg);
        return "redirect:" + url;
    }

    @GetMapping("/pwfind")
    public void pwfindForm() {

    }

    @PostMapping("/pwfind")
    public String pwfindOK(String user_id, String user_name, String user_email, String authcode, HttpSession session, RedirectAttributes rttr) throws Exception {

        String url = "";
        String msg = "";

        if(authcode.equals(session.getAttribute("authcode"))) {

        String d_email = userService.pwfind(user_id, user_name, user_email);
        if(d_email != null) {
            String tempPW = userService.getTempPw();

            String temp_enc_pw = passwordEncoder.encode(tempPW);

            userService.tempPwUpdate(user_id, temp_enc_pw);

            EmailDTO dto = new EmailDTO("THISTLE", "THISTLE", d_email, "THISTLE에서 임시 비밀번호를 보냈습니다", tempPW);
            emailService.sendMail("emailIDResult", dto, tempPW);

            session.removeAttribute("authcode");
            msg = "success";
            url = "/user/pwfind";
        } else {
            msg = "failInput";
            url = "/user/pwfind";
        }

        } else {
            msg = "failAuth";
            url = "/user/pwfind";
        }
        rttr.addFlashAttribute("msg", msg);
        return "redirect:" + url;
    }

    @GetMapping("mypage")
    public void mypageForm() {
        log.info("mypage");
    }

    @PostMapping("/modify")
    public String modifyOK(UserVO vo) throws Exception {

        return "redirect:/";
    }

}
