package com.docmall.thistle.admin;

public interface AdminMapper {

    void adminjoin(AdminVO vo);

    String idcheck(String admin_id);

    AdminVO loginOK(String admin_id);

}
