package com.docmall.thistle.admin.adminorder;

import com.docmall.thistle.common.constants.Constants;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.common.dto.PageDTO;
import com.docmall.thistle.common.util.FileManagerUtils;
import com.docmall.thistle.order.OrderVO;
import com.docmall.thistle.payinfo.PayInfoService;
import com.docmall.thistle.payinfo.PayInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/order/*")
@Controller
public class AdminOrderController {

    private final AdminOrderService adminOrderService;
    private final PayInfoService payInfoService;

    //상품 이미지 경로
    @Value("${file.product.image.dir}")
    private String uploadPath;

    //주문 목록
    @GetMapping("/order_list")
    public void order_listForm(Criteria cri,
                               @ModelAttribute("start_date") String start_date,
                               @ModelAttribute("end_date") String end_date,
                               Model model) throws Exception {

        cri.setAmount(Constants.ADMIN_ORDER_LIST_AMOUNT); //상수를 관리하는 CONSTANTS에서 작업하여 일괄 처리.

        List<OrderVO> order_list = adminOrderService.order_list(cri, start_date, end_date);

        int totalCount = adminOrderService.getTotalCount(cri, start_date, end_date);

        log.info("pagedto" + new PageDTO(cri, totalCount));

        model.addAttribute("order_list", order_list);
        model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
    }

    //주문목록 2. mybatis의 resulttype을 hashmap으로 사용하는 예제.
    @GetMapping("order_list2")
    public String order_list2(Model model) throws Exception {
        List<Map<String, Object>> order_list = adminOrderService.order_list2();

        model.addAttribute("order_list", order_list);

        return "redirect:/admin/order/order_list_map";
    }


    @GetMapping("/order_detail_info")
    public ResponseEntity<Map<String, Object>> order_detail_infoFrom(Long ord_code, Model model) throws Exception {
        ResponseEntity<Map<String, Object>> entity = null;

        Map<String, Object> map = new HashMap<>();

        //1) 주문자 정보
        OrderVO vo = adminOrderService.order_info(ord_code);
        map.put("ord_basic", vo);
        
        //2) 주문상품 정보
        List<OrderDetailinfoVO> ord_product_list = adminOrderService.order_detail_info(ord_code);

        ord_product_list.forEach(ord_pro -> {
                    ord_pro.setPro_up_folder(ord_pro.getPro_up_folder().replace("\\", "/"));
                });

            map.put("ord_pro_list", ord_product_list);

        //3) 결제 정보
        PayInfoVO p_vo = payInfoService.ord_pay_info(ord_code);
        map.put("payinfo", p_vo);

        entity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

        return entity;
    }

    // 주문상세정보에서 주문한 주문상품 이미지보여주기. 1)<img src="매핑주소"> 2) <img src="test.gif">
    @GetMapping("/image_display")
    public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {

        return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
    }


    //상품 개별 삭제
    @GetMapping("/order_product_delete")
    public ResponseEntity<String> order_product_deleteOK(Long ord_code, int pro_num) throws Exception {
        ResponseEntity<String> entity = null;

        adminOrderService.order_product_delete(ord_code, pro_num);

        entity = new ResponseEntity<String>("success", HttpStatus.OK);

        return entity;
    }

    // 상품 개별 수정
    @PostMapping("/order_basic_modify")
    public ResponseEntity<String> order_basic_modifyOK(OrderVO vo) throws Exception {
        ResponseEntity<String> entity = null;

        adminOrderService.order_basic_modify(vo);

        entity = new ResponseEntity<String>("success", HttpStatus.OK);
        return entity;
    }

    //삭제
    @PostMapping("/order_list_delete")
    public String order_list_deleteOK(Criteria cri, Long ord_code) throws Exception {

        adminOrderService.order_list_delete(ord_code);

        return "redirect:/admin/order/order_list";
    }
}
