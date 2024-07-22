package com.docmall.thistle.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminMapper adminMapper;

    public AdminVO loginOK(String admin_id) {
        return adminMapper.loginOK(admin_id);
    }
}
