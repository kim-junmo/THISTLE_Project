package com.docmall.thistle.admin;

import com.docmall.thistle.admin.product.ProductVO;
import com.docmall.thistle.order.OrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminMapper adminMapper;

    public void adminjoin(AdminVO vo) {
        adminMapper.adminjoin(vo);
    }

    public  String idcheck(String admin_id) {
        return adminMapper.idcheck(admin_id);
    }

    public AdminVO loginOK(String admin_id) {
        return adminMapper.loginOK(admin_id);
    }

    public int adminCount() {
        return adminMapper.adminCount();
    }

    public void updateVisitDate(String admin_id) {
        adminMapper.updateVisitDate(admin_id);
    }

    public DashboardVO getDashboardSummary() {
        return adminMapper.getDashboardSummary();
    }

    public List<OrderVO> getRecentOrders() {
        return adminMapper.getRecentOrders();
    }

    public List<ProductVO> getLowStockProducts() {
        return adminMapper.getLowStockProducts();
    }
}
