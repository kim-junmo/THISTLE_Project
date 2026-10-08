package com.docmall.thistle.admin.category;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminCategoryMapper {

    // 전체 카테고리 (상품 수 포함)
    List<AdminCategoryVO> getAllCategories();

    // 카테고리 추가 (cate_prtcode가 null이면 1차 카테고리)
    void cate_insert(@Param("cate_name") String cate_name, @Param("cate_prtcode") Integer cate_prtcode);

    // 카테고리 이름 수정
    void cate_update(@Param("cate_code") Integer cate_code, @Param("cate_name") String cate_name);

    // 카테고리 삭제
    void cate_delete(Integer cate_code);

    // 하위(2차) 카테고리 수
    int getChildCount(Integer cate_code);

    // 해당 카테고리에 등록된 상품 수
    int getProductCount(Integer cate_code);

    // 같은 부모 아래 같은 이름이 있는지
    int getSameNameCount(@Param("cate_name") String cate_name, @Param("cate_prtcode") Integer cate_prtcode);
}
