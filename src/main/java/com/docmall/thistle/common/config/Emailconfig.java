package com.docmall.thistle.common.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
@PropertySource("classpath:mail/email.properties")
public class Emailconfig {

    public Emailconfig() throws Exception {
        log.info("EmailConfig.java constructor called.");
    }

    //email.properties를 참고하여 만들어야 함.
    @Value("${spring.mail.transport.protocol}")
    private String protocol;

    @Value("${spring.mail.debug}")
    private boolean debug;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.default-encoding}")
    private String encoding;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Bean //JavaMailSender Bean 생성
    public JavaMailSender javaMailSender() {

        //JavaMailSenderImpl 객체 생성
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttls);

        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setPort(port);
        mailSender.setJavaMailProperties(properties);
        mailSender.setDefaultEncoding(encoding);

        log.info("메일서버: " + host);

        return mailSender;
    }

}
