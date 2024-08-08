package com.docmall.thistle.myshop;

import com.docmall.thistle.admin.adminorder.OrderDetailinfoVO;
import com.docmall.thistle.admin.product.ProductVO;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.common.dto.PageDTO;
import com.docmall.thistle.common.util.FileManagerUtils;
import com.docmall.thistle.user.UserVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/myshop/*")
@RequiredArgsConstructor
@Controller
public class MyshopController {

    //상품 이미지 경로
    @Value("${file.product.image.dir}")
    private String uploadPath;

    private final MyshopService myshopService;

    //주문정보
    @GetMapping("/order_list")
    public void order_ListForm(Criteria cri,
                               @ModelAttribute("start_date") String start_date,
                               @ModelAttribute("end_date") String end_date,
                               HttpSession session, Model model) throws Exception {

        //user_id 주입
        String user_id = ((UserVO) session.getAttribute("login_status")).getUser_id();

        List<OrderDetailinfoVO> ord_list = myshopService.order_list(cri, user_id, start_date, end_date);
        List<String> displayedDates = new ArrayList<>();

        ord_list.forEach(vo -> {
            vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
        });

        int totalCount = myshopService.getTotalCount(cri, user_id, start_date, end_date);

        model.addAttribute("ord_list", ord_list);
        model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
        model.addAttribute("displayedDates", displayedDates);

    }

    // 주문상세정보에서 주문한 주문상품 이미지보여주기. 1)<img src="매핑주소"> 2) <img src="test.gif">
    @GetMapping("/image_display")
    public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {

        return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
    }

}
