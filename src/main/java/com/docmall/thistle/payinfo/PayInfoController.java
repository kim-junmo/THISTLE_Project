package com.docmall.thistle.payinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payinfo/*")
@Controller
public class PayInfoController {

    private final PayInfoService payInfoService;
}
