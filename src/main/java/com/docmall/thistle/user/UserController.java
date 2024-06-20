package com.docmall.thistle.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
    private final UserService userService;

    //비밀번호 저장 시 보안을 위해 만듬(서버로 비밀번호가 저장될 시 60자로 변경)
    //PasswordEncoder를 사용하기 위해 SecurityConfig에 bean을 설정
    private final PasswordEncoder passwordEncoder;

//    private final EmailService emailService;

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

}
