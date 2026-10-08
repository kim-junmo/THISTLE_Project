package com.docmall.thistle.admin.stats;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatsMapper {

    // 해당 연도의 월별 매출 (주문이 없는 달은 결과에 없음)
    List<MonthlySalesVO> getMonthlySales(@Param("year") String year, @Param("cancel_status") String cancel_status);

    // 기간별 1차 카테고리 매출
    List<CategorySalesVO> getCategorySales(@Param("start_date") String start_date,
                                           @Param("end_date") String end_date,
                                           @Param("cancel_status") String cancel_status);

    // 주문이 처음 들어온 연도 (연도 선택 목록용, 주문이 없으면 null)
    Integer getFirstOrderYear();
}
