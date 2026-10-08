package com.docmall.thistle.admin.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/category/*")
@Controller
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    // 카테고리 목록
    @GetMapping("/cate_list")
    public void cate_listForm(Model model) {
        model.addAttribute("cate_tree", adminCategoryService.getCategoryTree());
    }

    // 카테고리 추가 (cate_prtcode가 없으면 1차, 있으면 2차 카테고리)
    @PostMapping("/cate_insert")
    public String cate_insertOK(String cate_name, Integer cate_prtcode, RedirectAttributes rttr) {

        String error = adminCategoryService.cate_insert(cate_name, cate_prtcode);
        rttr.addFlashAttribute("msg", error == null ? "insertSuccess" : error);

        return "redirect:/admin/category/cate_list";
    }

    // 카테고리 이름 수정
    @PostMapping("/cate_update")
    public String cate_updateOK(Integer cate_code, String cate_name, RedirectAttributes rttr) {

        String error = adminCategoryService.cate_update(cate_code, cate_name);
        rttr.addFlashAttribute("msg", error == null ? "updateSuccess" : error);

        return "redirect:/admin/category/cate_list";
    }

    // 카테고리 삭제
    @PostMapping("/cate_delete")
    public String cate_deleteOK(Integer cate_code, RedirectAttributes rttr) {

        String error = adminCategoryService.cate_delete(cate_code);
        rttr.addFlashAttribute("msg", error == null ? "deleteSuccess" : error);

        return "redirect:/admin/category/cate_list";
    }
}
