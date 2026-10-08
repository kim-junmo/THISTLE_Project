package com.docmall.thistle.admin.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

// 관리자 카테고리 관리 화면용. 1차 카테고리 아래에 2차 카테고리 목록(children)을 담는다.
@Getter
@Setter
@ToString
public class AdminCategoryVO {

    private Integer cate_code;
    private Integer cate_prtcode;   // 1차 카테고리는 null
    private String cate_name;
    private int pro_count;          // 이 카테고리에 등록된 상품 수 (삭제 가능 여부 판단용)

    private List<AdminCategoryVO> children = new ArrayList<>();
}
