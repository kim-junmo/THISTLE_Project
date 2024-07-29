package com.docmall.thistle.order;

import com.docmall.thistle.cart.CartProductVO;
import com.docmall.thistle.cart.CartService;
import com.docmall.thistle.cart.CartVO;
import com.docmall.thistle.user.UserService;
import com.docmall.thistle.user.UserVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order/*")
@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService; //주문자와 동일때문에 추가

    /*
	 1) pro_list(modal), 2) pro_detail 바로구매, 3)장바구니에서 주문하기
	 1, 2번은 cartvo vo 파라미터를 사용한다, 3)번은 cartvo vo 파라미터를 사용하지 않기 때문에 필요가 없어 정보도 없다.
	 */

    @GetMapping("/orderinfo")
    public String orderinfoForm(@RequestParam(value = "type", defaultValue = "direct") String type, CartVO vo, Model model, HttpSession session) throws Exception {

        String user_id = ((UserVO) session.getAttribute("login_status")).getUser_id();
        vo.setUser_id(user_id);

        //상품페이지에서 바로구매 시 장바구니에서 추가하여 바로구매 페이지로 이동(direct)
        if (type.equals("direct")) {
            cartService.cart_add(vo);
        }

//        if(type.equals("cartorder")) {
//            //장바구니 저장
//            cartService.cart_add(vo);

            //주문하기 공통사항(장바구니 및 상품페이지 바로구매 둘다)
            List<CartProductVO> cart_list = cartService.cart_list(user_id);
            int total_price = 0;
            cart_list.forEach(d_vo -> {
                d_vo.setPro_up_folder(d_vo.getPro_up_folder().replace("\\", "/"));
            }
            );

            //총 합계 금액
            for(int i=0; i < cart_list.size(); i++) {
                total_price += (cart_list.get(i).getPro_price() * cart_list.get(i).getCart_amount());


            model.addAttribute("cart_list", cart_list);
            model.addAttribute("total_price", total_price);
        }
        return "/order/orderinfo";
    }

    //주문자와 동일
    @GetMapping("/ordersame")
    public ResponseEntity<UserVO> ordersameOK(HttpSession session) throws Exception {

        ResponseEntity<UserVO> entity = null;

        String user_id = ((UserVO) session.getAttribute("login_status")).getUser_id();
        entity = new ResponseEntity<UserVO>(userService.login(user_id), HttpStatus.OK); //회원정보를 가지고 온다.

        return entity;
    }

    //무통장 입금
    @PostMapping("/ordersave")
    public String ordersaveOK(OrderVO vo, String pay_nobank, String pay_nobank_user, HttpSession session) throws Exception {

        log.info("주문정보 : " + vo);
        log.info("입금 은행 : " + pay_nobank);
        log.info("주문자 : " + pay_nobank_user);

        String user_id = ((UserVO) session.getAttribute("login_status")).getUser_id();
        vo.setUser_id(user_id);

        String payinfo = pay_nobank + "/" + pay_nobank_user;

        orderService.order_process(vo, user_id, "무통장입금", "미납", payinfo);

        return "redirect:/order/ordercomplete";
    }

    //주문완료
    @GetMapping("/ordercomplete")
    public void ordercompleteForm() throws Exception {

    }


}
