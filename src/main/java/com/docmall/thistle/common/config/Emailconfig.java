package com.docmall.thistle.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Configuration
@PropertySource("classpath:mail/email.properties")
public class Emailconfig {

    public Emailconfig() {
        log.info("...");
    }
}
