package com.docmall.thistle.common.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EmailDTO {

    private String senderName; //발신자 이름
    private String senderMail; //발신자 메일주소
    private String receiverMail; //수신자 메일주소
    private String subject; //제목
    private String message; //내용

    public EmailDTO() {
        this.senderMail = "THISTLE";
        this.senderName = "THISTLE";
        this.subject = "THISTLE 회원가입 메일인증 코드입니다.";
        this.message = "방문해주셔서 감사합니다. 메일 인증 코드를 확인하시고, 회원가입시 인증코드 입력칸에 입력바랍니다.";
    }
}
