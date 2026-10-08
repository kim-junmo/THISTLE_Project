package com.docmall.thistle.admin.stats;

import com.docmall.thistle.common.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsService {

    private final StatsMapper statsMapper;

    /**
     * 1월~12월 12칸을 항상 채워서 돌려준다. (주문이 없는 달은 0)
     */
    public List<MonthlySalesVO> getMonthlySales(int year) {

        List<MonthlySalesVO> result = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            result.add(new MonthlySalesVO(m, 0, 0));
        }

        for (MonthlySalesVO vo : statsMapper.getMonthlySales(String.valueOf(year), Constants.ORDER_STATUS_CANCEL)) {
            if (vo.getMonth() >= 1 && vo.getMonth() <= 12) {
                result.set(vo.getMonth() - 1, vo);
            }
        }
        return result;
    }

    public List<CategorySalesVO> getCategorySales(String start_date, String end_date) {
        return statsMapper.getCategorySales(start_date, end_date, Constants.ORDER_STATUS_CANCEL);
    }

    public Integer getFirstOrderYear() {
        return statsMapper.getFirstOrderYear();
    }
}
