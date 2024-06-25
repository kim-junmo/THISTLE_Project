package com.docmall.thistle.common.mail;

import com.docmall.thistle.common.constants.constants;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;  // JavaMailSender를 사용해 이메일을 보냄

    private final SpringTemplateEngine springTemplateEngine;  // 스프링 템플릿 엔진
    private final SpringTemplateEngine templateEngine;  // 스프링 템플릿 엔진 (중복 선언, 하나만 필요함)

    public void sendMail(String type, EmailDTO dto, String authcode) {  // 이메일을 보내는 메서드
        type = constants.MAILFOLDNAME + "/" + type;  // 이메일 템플릿 경로 설정

        MimeMessage mimeMessage = mailSender.createMimeMessage();  // 이메일 메시지 생성

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");  // 메시지 헬퍼 초기화
            mimeMessageHelper.setTo(dto.getReceiverMail());  // 수신자 설정
            mimeMessageHelper.setFrom(new InternetAddress(dto.getSenderMail(), dto.getSenderName()));  // 발신자 설정
            mimeMessageHelper.setSubject(dto.getSubject());  // 제목 설정
            mimeMessageHelper.setText(setContext(authcode, type), true);  // 내용 설정 (HTML 가능)

            mailSender.send(mimeMessage);  // 이메일 발송
        } catch (Exception e) {
            e.printStackTrace();  // 오류 발생 시 스택 트레이스 출력
        }
    }

    public String setContext(String authcode, String type) {  // 이메일 내용 설정
        Context context = new Context();  // 템플릿 엔진의 컨텍스트 생성
        context.setVariable("authcode", authcode);  // 인증 코드를 컨텍스트에 설정

        return templateEngine.process(type, context);  // 템플릿 엔진으로 이메일 내용 생성
    }
}
