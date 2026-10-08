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

    // 컨트롤러는 모든 사용자가 같이 쓰는 객체(싱글톤)라서, 주문정보를 필드에 저장하면
    // 동시에 결제하는 다른 사용자의 정보로 덮어써진다. 그래서 사용자별 세션에 저장한다.
    private static final String SESSION_ORDER = "kakaopay_order";
    private static final String SESSION_TID = "kakaopay_tid";

    @GetMapping("/kakaopayrequest")
    public void kakaopayrequest() {

    }

    @ResponseBody
    @GetMapping(value = "/kakaopay")
    public ReadyResponse kakaopay(OrderVO vo, HttpSession session) {
        log.info("사용자 정보 :" + vo);

        //1. 결제 준비요청
        String user_id = ((UserVO) session.getAttribute("login_status")).getUser_id();

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

        // 승인 단계에서 쓸 주문정보와 결제고유번호(tid)를 이 사용자의 세션에 보관
        session.setAttribute(SESSION_ORDER, vo);
        session.setAttribute(SESSION_TID, readyResponse.getTid());

        return readyResponse;
    }

    //성공
    @GetMapping("/approval")
    public String approval(String pg_token, HttpSession session) {
        log.info("pg_token : " + pg_token);

        OrderVO vo = (OrderVO) session.getAttribute(SESSION_ORDER);
        String tid = (String) session.getAttribute(SESSION_TID);
        UserVO user = (UserVO) session.getAttribute("login_status");

        // 결제 준비 정보가 없으면(세션 만료 등) 승인할 수 없으므로 실패 화면으로
        if (vo == null || tid == null || user == null) {
            return "kakao/fail";
        }
        String user_id = user.getUser_id();

        //2) 결제 승인 요청
        String approveResponse = kakaoPayService.approve(pg_token, tid, user_id, user_id);
        log.info("최종결과 : " + approveResponse);

        session.removeAttribute(SESSION_ORDER);
        session.removeAttribute(SESSION_TID);

        if(approveResponse != null && approveResponse.contains("aid")) {
            log.info("주문자 정보 2 : " + vo);
            orderService.order_process(vo, user_id, "kakaopay", "결제완료", "kakaopay");
            return "kakao/approval";
        }
        return "kakao/fail";
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
