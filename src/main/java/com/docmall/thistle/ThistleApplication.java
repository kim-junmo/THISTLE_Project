
package com.docmall.thistle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//com.docmall.thistle에 있는 모든 인터페이스를 mapper로 인식. 그렇기 때문에 service인터페이스는 삭제!
//service를 사용할 수 없기 때문에 ServiceImpl을 Service로 변경함.
@MapperScan(basePackages = "com.docmall.thistle.**")
//exclude = SecurityAutoConfiguration.class 스프링 시큐리트 적용을 하지 못하게 만듬
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ThistleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThistleApplication.class, args);
    }

}
