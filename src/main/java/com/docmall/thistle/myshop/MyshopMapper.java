package com.docmall.thistle.myshop;


import com.docmall.thistle.admin.adminorder.OrderDetailinfoVO;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.order.OrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MyshopMapper {

    List<OrderDetailinfoVO> order_list(@Param("cri") Criteria cri, @Param("user_id") String user_id, @Param("start_date") String start_date, @Param("end_date") String end_date);

    int getTotalCount(@Param("cri") Criteria cri, @Param("user_id") String user_id, @Param("start_date") String start_date, @Param("end_date") String end_date);


}
