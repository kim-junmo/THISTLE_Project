package com.docmall.thistle;

import com.docmall.thistle.common.category.CategoryService;
import com.docmall.thistle.common.category.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@ControllerAdvice(basePackages = {"com.docmall.thistle.product","com.docmall.thistle.cart", "com.docmall.thistle.order", "com.docmall.thistle.user", "com.docmall.thistle.myshop"})
public class GlobalControllerAdvice {

    private final CategoryService categoryService;

    @ModelAttribute
    public void comm_test(Model model) {
        log.info("공통코드 실행");

        List<CategoryVO> cate_list = categoryService.getFirstCategoryList();
        model.addAttribute("user_cate_list", cate_list);
    }

}
