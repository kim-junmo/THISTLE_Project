package com.docmall.thistle;

import com.docmall.thistle.common.category.CategoryService;
import com.docmall.thistle.common.category.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class HomeController {

    private final CategoryService categoryService;

    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("기본주소");

        List<CategoryVO> cate_list = categoryService.getFirstCategoryList();
        model.addAttribute("user_cate_list", cate_list);

        return "index";
    }
}
