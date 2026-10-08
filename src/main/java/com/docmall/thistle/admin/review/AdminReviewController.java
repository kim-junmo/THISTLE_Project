package com.docmall.thistle.admin.review;

import com.docmall.thistle.common.constants.Constants;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.common.dto.PageDTO;
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
@RequestMapping("/admin/review/*")
@Controller
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    // 리뷰 목록 (검색: P 상품명, I 아이디, C 제목/내용)
    @GetMapping("/review_list")
    public void review_listForm(Criteria cri, Model model) {

        cri.setAmount(Constants.ADMIN_REVIEW_LIST_AMOUNT);

        model.addAttribute("review_list", adminReviewService.review_list(cri));
        model.addAttribute("pageMaker", new PageDTO(cri, adminReviewService.getTotalCount(cri)));
    }

    // 리뷰 삭제 (부적절한 리뷰 정리용). 삭제 후 보던 페이지로 돌아간다.
    @PostMapping("/review_delete")
    public String review_deleteOK(Criteria cri, Long re_code, RedirectAttributes rttr) {

        if (re_code != null) {
            adminReviewService.review_delete(re_code);
            rttr.addFlashAttribute("msg", "deleteSuccess");
        }

        return "redirect:/admin/review/review_list" + cri.getListLink();
    }
}
