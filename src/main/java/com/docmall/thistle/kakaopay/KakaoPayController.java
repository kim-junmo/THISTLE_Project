package com.docmall.thistle.kakaopay;

import com.docmall.thistle.cart.CartProductVO;
import com.docmall.thistle.cart.CartService;
import com.docmall.thistle.order.OrderService;
import com.docmall.thistle.order.OrderVO;
import com.docmall.thistle.user.UserVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/kakao/*")
@Controller
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final CartService cartService;
    private final OrderService orderService;

    private OrderVO vo;
    private String user_id;

    @GetMapping("/kakaopayrequest")
    public void kakaopayrequest() {

    }

    @ResponseBody
    @GetMapping(value = "/kakaopay")
    public ReadyResponse kakaopay(OrderVO vo, HttpSession session) {
        log.info("사용자 정보 :" + vo);

        //1. 결제 준비요청
        String user_id = ((UserVO) session.getAttribute("login_status")).getUser_id();
        this.user_id = user_id;

        //장바구니 주문 목록
        List<CartProductVO> cart_list = cartService.cart_list(user_id);

        String itemName = "";
        int quantity = 0;
        int totalAmount = 0;
        int taxFreeAmount = 0;
        int vatAmount = 0;

        for (int i = 0; i < cart_list.size(); i++) {
            itemName += cart_list.get(i).getPro_name();
            quantity += cart_list.get(i).getCart_amount();
            totalAmount += cart_list.get(i).getPro_price() * cart_list.get(i).getCart_amount();
        }

        String partnerOrderId = user_id;
        String partnerUserId = user_id;

        //1) 카카오페이 결제 준비 요청
        ReadyResponse readyResponse = kakaoPayService.ready(partnerOrderId, partnerUserId, itemName, quantity, totalAmount, taxFreeAmount, vatAmount);

        log.info("응답 데이터 : " + readyResponse);

        this.vo = vo;

        return readyResponse;
    }

    //성공
    @GetMapping("/approval")
    public void approval(String pg_token) {
        log.info("pg_token : " + pg_token);

        //2) 결제 승인 요청
        String approveResponse = kakaoPayService.approve(pg_token);
        log.info("최종결과 : " + approveResponse);

        if(approveResponse.contains("aid")) {
            log.info("주문자 정보 2 : " + vo);
            orderService.order_process(vo, user_id, "kakaopay", "결제완료", "kakaopay");
        }
    }

    //취소
    @GetMapping("/cancel")
    public void cancel() {
    }

    //실패
    @GetMapping("/fail")
    public void fail() {
    }
}
