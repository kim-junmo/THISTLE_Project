package com.docmall.thistle.admin;

import com.docmall.thistle.admin.product.ProductVO;
import com.docmall.thistle.order.OrderVO;

import java.util.List;

public interface AdminMapper {

    void adminjoin(AdminVO vo);

    String idcheck(String admin_id);

    AdminVO loginOK(String admin_id);

    // 등록된 관리자 수 (0이면 최초 관리자 등록 허용)
    int adminCount();

    // 최근 접속일 기록
    void updateVisitDate(String admin_id);

    // 대시보드 요약
    DashboardVO getDashboardSummary();

    // 최근 주문 5건
    List<OrderVO> getRecentOrders();

    // 재고가 적은 상품 5개
    List<ProductVO> getLowStockProducts();

}
