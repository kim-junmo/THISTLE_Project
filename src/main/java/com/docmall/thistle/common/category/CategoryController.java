package com.docmall.thistle.common.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/category/*")
public class CategoryController {

    private final CategoryService categoryService;

    //2차 카테고리 목록
    @GetMapping("/secondcategory/{cate_prtcode}")
    public ResponseEntity<List<CategoryVO>> getSecondcategorylist(@PathVariable("cate_prtcode") int cate_prtcode) throws Exception {
        log.info("1차 카테고리 : " + cate_prtcode);

        ResponseEntity<List<CategoryVO>> entity = null;

        entity = new ResponseEntity<List<CategoryVO>>(categoryService.getSecondCategoryList(cate_prtcode), HttpStatus.OK);

        return entity;
    }
}
