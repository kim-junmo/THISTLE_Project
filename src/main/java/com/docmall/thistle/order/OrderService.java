package com.docmall.thistle.order;

import com.docmall.thistle.cart.CartMapper;
import com.docmall.thistle.payinfo.PayInfoMapper;
import com.docmall.thistle.payinfo.PayInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final PayInfoMapper payInfoMapper;
    private final CartMapper cartMapper;


    //카카오 페이 주문정보 트래지션에 사용하기 위해 만듬.
    // 1) 주문테이블(insert) 2) 주문 상세테이블(insert) 3) 결제 테이블(insert) 4) 장바구니 테이블(delete)
    //트랜잭션 : 데이터베이스의 상태를 변화시키기 위한 작업, 여러 개의 데이터베이스 작업(주문 삽입, 주문 상세 삽입, 결제 정보 삽입, 장바구니 삭제)을 포함.
    //이 작업들이 모두 성공적으로 수행되어야만 전체 주문 프로세스가 완료가 되며 만약 이 중 어느 하나라도 실패하면, 모든 작업이 롤백된다.
    //해당 작업은 sql상에서도 작업이 가능하지만 복잡하기 때문에 여기서 작업함.
    @Transactional
    public void order_process(OrderVO vo, String user_id, String paymethod, String p_status, String payinfo) {

        //주문 테이블
        vo.setUser_id(user_id);
        orderMapper.order_insert(vo);

        //주문 상세 테이블
        orderMapper.orderDetail_insert(vo.getOrd_code(), user_id);

        //결제 테이블(INSERT) 작업을 하기 위해 payinfo mapper작업을 함.
        //builder를 사용하기 위해서는 PayInfoVO에 builder 어노테이션 추가!
        PayInfoVO p_vo = PayInfoVO.builder()
                .ord_code(vo.getOrd_code())
                .user_id(user_id)
                .p_price(vo.getOrd_price())
                .payinfo(payinfo)
                .paymethod(paymethod)
                .p_status(p_status)
                .build();

        payInfoMapper.payinfo_insert(p_vo);

        //장바구니 테이블(delete)
        cartMapper.cart_empty(user_id);
    }
}
