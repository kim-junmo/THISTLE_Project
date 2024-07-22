package com.docmall.thistle.common.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<CategoryVO> getFirstCategoryList() {
        return categoryMapper.getFirstCategoryList();
    }

    public List<CategoryVO> getSecondCategoryList(int cate_prtcode) {
        return categoryMapper.getSecondCategoryList(cate_prtcode);
    }

    public CategoryVO getFirstCategoryBySecondCategory(int cate_prtcode) {
        return categoryMapper.getFirstCategoryBySecondCategory(cate_prtcode);
    }
}
