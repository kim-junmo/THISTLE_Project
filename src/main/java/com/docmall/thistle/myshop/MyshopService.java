package com.docmall.thistle.myshop;

import com.docmall.thistle.admin.adminorder.OrderDetailinfoVO;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.order.OrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class MyshopService {

    private final MyshopMapper myshopMapper;

    public List<OrderDetailinfoVO> order_list(Criteria cri, String user_id, String start_date, String end_date) {
        return myshopMapper.order_list(cri, user_id, start_date, end_date);
    }

    public int getTotalCount(Criteria cri, String user_id, String start_date, String end_date) {
        return myshopMapper.getTotalCount(cri, user_id, start_date, end_date);
    }
}
