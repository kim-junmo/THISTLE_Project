package com.docmall.thistle.common.category;

import java.util.List;

public interface CategoryMapper {

    //1차 카테고리 정보
    List<CategoryVO> getFirstCategoryList();

    //2차 카테고리 정보
    List<CategoryVO> getSecondCategoryList(int cate_prtcode);
    
    //2차 카테고리 정보를 이용한 1차 카테고리 정보
    CategoryVO getFirstCategoryBySecondCategory(int cate_prtcode);
    
}
