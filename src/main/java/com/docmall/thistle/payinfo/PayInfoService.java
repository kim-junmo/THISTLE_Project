package com.docmall.thistle.payinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PayInfoService {

    private final PayInfoMapper payInfoMapper;

}
