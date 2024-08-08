package com.docmall.thistle.admin.user;

import com.docmall.thistle.common.constants.Constants;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.common.dto.PageDTO;
import com.docmall.thistle.common.mail.EmailDTO;
import com.docmall.thistle.common.mail.EmailService;
import com.docmall.thistle.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@RequestMapping("/admin/user/*")
@RequiredArgsConstructor
@Controller
public class AdminUserController {

    private final AdminUserService adminUserService;

    private final EmailService emailService;

    @Value("${file.ckdir}")
    private String uploadCKPath;

    @GetMapping("user_list")
    public void user_listForm(Criteria cri,
                              @ModelAttribute("start_date") String start_date,
                              @ModelAttribute("end_date") String end_date,
                              Model model) {

        cri.setAmount(Constants.ADMIN_USER_LIST_AMOUNT);

        List<UserVO> user_list = adminUserService.user_list(cri, start_date, end_date);
        int totalCount = adminUserService.getTotalCount(cri, start_date, end_date);

        log.info("pagedto" + new PageDTO(cri, totalCount));

        model.addAttribute("user_list", user_list);
        model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
    }

    @GetMapping("/user_delete")
    public ResponseEntity<String> user_deleteOK(String user_id) {
        ResponseEntity<String> entity = null;

        //db연동
        adminUserService.user_delete(user_id);

        entity = new ResponseEntity<String>("success", HttpStatus.OK);
        return entity;
    }

    //메일 발송 목록
    @GetMapping("/mailinglist")
    public void mailinglistForm(Criteria cri, String title, Model model) throws Exception {

        cri.setAmount(Constants.ADMIN_MAILING_LIST_AMOUNT);
        List<MailmngVO> maillist = adminUserService.mailingInfoList(cri, title);

        int totalMailCount = adminUserService.MailListCount(title);

        PageDTO dto = new PageDTO(cri, totalMailCount);

        model.addAttribute("maillist", maillist);
        model.addAttribute("pageMaker", dto);
    }

    //일반 메서드를 호출하는 경우, 파라미터(매개변수) 값을 제공해야 한다. (@GetMapping이 없을 경우)
    //주소에 의하여 호출되는 메서드는 파라미터를 스프링이 관여하여 객체를 먼저 생성한다.
    //그리고 사용자가 입력한 값이 setter메서드에 의하여 객체에 저장된다.
    //메일발송 폼(ckeditor 사용) - 구분 1. 광고/이벤트 2. 일반
    @GetMapping("/mailingform")
    public void mailingForm(@ModelAttribute("vo") MailmngVO vo) {

    }

    //메일 저장
    @PostMapping("/mailingsave")
    public String mailingsaveOK(@ModelAttribute("vo") MailmngVO vo, Model model) throws Exception {

        log.info("메일 내용 : " + vo);

        adminUserService.mailing_save(vo);

        model.addAttribute("idx", vo.getIdx());

        //redirect를 사용하면 다시 mailingform페이지로 찾아가지만, 사용하지 않으면 타임리프 혹은 jsp로 인식을 하기 때문에 이전 작성한 데이터가 남아 있는다.
        return "/admin/user/mailingform";  //redirect를 사용 안할경우 주소가 아니라 타임리프 파일명이 된다.
    }

    //메일 발송
    @PostMapping("/mailingsend")
    public String mailprocess(MailmngVO vo, RedirectAttributes rttr) throws Exception {

        //1. 메일 발송
        //1-1. 회원테이블에서 전체 회원 메일 정보를 읽어오는 작업
        //배열을 사용하는 이유는 회원테이블에 회원 정보는 정해져 있기 때문이다.
        String[] emailArr = adminUserService.getAllMailAddress();

        //1-2. 메일 발송
//        EmailDTO dto = new EmailDTO("DocMall", "DocMall", "수신자메일주소", "제목", "내용");
        EmailDTO dto = new EmailDTO("THISTLE", "THISTLE", "", vo.getTitle(), vo.getContent());

        emailService.sendMail(dto, emailArr);

        //1-3. 메일 발송 횟수 업데이트
        adminUserService.mailSendCountUpdate(vo.getIdx());

        rttr.addAttribute("msg", "send");
        return "redirect:/admin/user/mailinglist";
    }

    @GetMapping("/mailingsendform")
    public void mailingsendform(int idx, Model model) throws Exception {

        MailmngVO vo = adminUserService.MailSendInfo(idx);

//        log.info("vo값은? " + vo);

        model.addAttribute("vo", vo);
    }

    @PostMapping("mailingedit")
    public String mailingeditOK(@ModelAttribute("vo") MailmngVO vo, Model model) throws Exception {

        adminUserService.mailingedit(vo);

        model.addAttribute("msg", "modify");

        return "redirect:/admin/user/mailinglist";
    }

    //ckeditor 상품 설명 이미지 업로드
    //MultipartFile upload : ckeditor 업로드 탭에서 나온 파일 첨부 태그 파라미터(<input type="file" name="upload">)를 참조함.
    //MultipartFile upload : 첨부된 파일정보를 가지고 있음.
    //HttpServletRequest request : 클라이언트의 요청정보를 가지고 있는 객체.
    //HttpServletResponse response : 서버에서 클라이언트에게 보낸 정보를 응답하는 객체.
    @PostMapping("/imageupload")
    public void imageupload(HttpServletRequest request, HttpServletResponse response, MultipartFile upload) {

        //입출력 스트림으로 OutputStream을 사용함. 자바의 입출력 스트림 공부해서 정리할 필요가 있음.
        OutputStream out = null;
        //PrintWriter: 서버에서 클라이언트에게 응답정보를 보낼때 사용.(업로드한 이미지 정보를 클라이언트(브라우저)에게 보내는 작업용도)
        PrintWriter printWriter = null;

        try {
            //1)ckeditor를 통한 파일 업로드 처리 기능.
            String OriginalfileName = upload.getOriginalFilename(); //업로드 할 클라이언트 파일 이름.
            String fileName = URLEncoder.encode(OriginalfileName, StandardCharsets.UTF_8.toString());
            byte[] bytes = upload.getBytes(); //업로드 할 파일의 바이트 배열

            if(!uploadCKPath.endsWith("/") && !uploadCKPath.endsWith("\\")) {
                uploadCKPath += System.getProperty("file.separator");
            }

            //uploadCKPath + fileName : "C:\\dev\\upload\\ckeditor\\" + "fileName(ex. abc.gif)
            //그렇기 때문에 경로에 \\가 들어가 있어야 함. 없으면 C:\\dev\\upload\\ckeditorabc.gif 이렇게 됨.
            String ckUploadPath = uploadCKPath + fileName;

            //C:\\dev\\upload\\ckeditor\\abc.gif 이 한줄에 의해 파일이 생성이 된다.
            //이때 파일은 0byte로 생성이 된다.
            out = new FileOutputStream(ckUploadPath);

            out.write(bytes); //빨때(스트림)의 공간에 업로드할 파일의 바이트 배열을 채운 상태.
            out.flush(); //0byte의 파일의 크기가 채워진 정상적인 파일로 인식이 된다.

            //2)업로드한 이미지 파일에 대한 정보를 클라이언트에게 보내는 작업.
            printWriter = response.getWriter();

            //이메일 첨부 시 이미지가 깨지는 문제 발생, 상수 Constants에 도메인 주소를 넣어 사용하면 깨지지 않음.
            String fileUrl = Constants.ROOT_URL + "/admin/user/display/" + fileName; //매핑주소/이미지파일명
            //String fileUrl = fileName;

            //ckeditor 4.1.2에서는 파일정보를 다음과 같이 구성하여 보내야 한다.
            //{"fileName" : "abc.gif", "uploaded":1, "url":"/ckupload/abc.gif"} : 자바스트의 제이슨 문법 스타일.
            //{"fileName" : "변수", "uploaded":1, "url":"변수"}
            //즉 변수 값을 포함하는 JSON 문자열을 생성하고 출력하는 예제이다. 스트림에 내용을 채움.
            printWriter.println("{\"fileName\" :\"" + OriginalfileName + "\", \"uploaded\":1,\"url\":\"" + fileUrl + "\"}"); //스프림에 채움.
            printWriter.flush(); //println의 정보가 브라우저로 보내진다.

        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if(out != null) {
                try {
                    out.close();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            if(printWriter != null) printWriter.close();
        }
    }

    //<img src="매핑주소">를 통해 이미지를 제공하는 핸들러
    @GetMapping("/display/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable("fileName") String fileName) {
        // 파일 이름 로그 출력
        log.info("파일이미지: " + fileName);

        ResponseEntity<byte[]> entity = null;

        try {
            // 파일 이름 디코딩
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());
            // 파일 객체 생성
            File file = new File(uploadCKPath + decodedFileName);

            // 파일이 존재하지 않으면 404 응답
            if (!file.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 파일 내용을 바이트 배열로 읽기
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file.toPath()));
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + decodedFileName + "\"");

            // 파일과 헤더를 포함한 ResponseEntity 생성
            entity = new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return entity;
    }

}
