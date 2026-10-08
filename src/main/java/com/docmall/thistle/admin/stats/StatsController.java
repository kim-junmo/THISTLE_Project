package com.docmall.thistle.admin.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// 메뉴(nav.html)에 이미 있던 주소 /admin/staticAnalysis/* 를 그대로 사용
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/staticAnalysis/*")
@Controller
public class StatsController {

    private final StatsService statsService;

    // 월별 매출현황
    @GetMapping("/monthly")
    public void monthly(Integer year, Model model) {

        int thisYear = LocalDate.now().getYear();
        if (year == null || year < 2000 || year > thisYear) {
            year = thisYear;
        }

        List<MonthlySalesVO> monthly_list = statsService.getMonthlySales(year);

        long total_sales = 0;
        int total_orders = 0;
        for (MonthlySalesVO vo : monthly_list) {
            total_sales += vo.getSales();
            total_orders += vo.getOrder_count();
        }

        // 연도 선택 목록: 첫 주문 연도 ~ 올해 (주문이 없으면 올해만)
        Integer firstYear = statsService.getFirstOrderYear();
        int startYear = (firstYear == null || firstYear > thisYear) ? thisYear : firstYear;
        List<Integer> year_list = new ArrayList<>();
        for (int y = thisYear; y >= startYear; y--) {
            year_list.add(y);
        }

        model.addAttribute("year", year);
        model.addAttribute("year_list", year_list);
        model.addAttribute("monthly_list", monthly_list);
        model.addAttribute("total_sales", total_sales);
        model.addAttribute("total_orders", total_orders);
    }

    // 카테고리별 매출현황 (기본: 이번 달 1일 ~ 오늘)
    @GetMapping("/orderstats")
    public void orderstats(String start_date, String end_date, Model model) {

        LocalDate today = LocalDate.now();
        LocalDate start = parseDate(start_date, today.withDayOfMonth(1));
        LocalDate end = parseDate(end_date, today);
        if (start.isAfter(end)) {
            LocalDate tmp = start;
            start = end;
            end = tmp;
        }

        List<CategorySalesVO> category_list = statsService.getCategorySales(start.toString(), end.toString());

        long total_sales = 0;
        int total_quantity = 0;
        for (CategorySalesVO vo : category_list) {
            total_sales += vo.getSales();
            total_quantity += vo.getQuantity();
        }

        model.addAttribute("start_date", start.toString());
        model.addAttribute("end_date", end.toString());
        model.addAttribute("category_list", category_list);
        model.addAttribute("total_sales", total_sales);
        model.addAttribute("total_quantity", total_quantity);
    }

    // "yyyy-MM-dd" 형식이 아니면 기본값 사용
    private LocalDate parseDate(String value, LocalDate defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }
}
