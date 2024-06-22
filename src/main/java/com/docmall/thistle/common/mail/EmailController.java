package com.docmall.thistle.common.mail;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/authcode")
    public ResponseEntity<String> authcode(String type, EmailDTO dto, HttpSession session) {

        log.info("DTO: " + dto);

        ResponseEntity<String> entity = null;

        return entity;
    }
}
