package com.docmall.thistle.common.mail;

import com.docmall.thistle.common.constants.constants;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine springTemplateEngine;

    public void sendMail(String type, EmailDTO dto, String authcode) {
        type = constants.MAILFOLDNAME + "/" + type;

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");

        }catch (Exception e) {
            e.printStackTrace();
        }


    }
}
