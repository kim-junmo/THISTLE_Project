package com.docmall.thistle.admin.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminCategoryService {

    private final AdminCategoryMapper adminCategoryMapper;

    // 1차 카테고리 목록 + 각 1차 카테고리의 2차 카테고리(children)
    public List<AdminCategoryVO> getCategoryTree() {

        List<AdminCategoryVO> all = adminCategoryMapper.getAllCategories();

        // 1차 카테고리를 먼저 담고(순서 유지), 2차 카테고리를 부모에 붙인다
        Map<Integer, AdminCategoryVO> parents = new LinkedHashMap<>();
        for (AdminCategoryVO vo : all) {
            if (vo.getCate_prtcode() == null) {
                parents.put(vo.getCate_code(), vo);
            }
        }
        for (AdminCategoryVO vo : all) {
            if (vo.getCate_prtcode() != null) {
                AdminCategoryVO parent = parents.get(vo.getCate_prtcode());
                if (parent != null) {
                    parent.getChildren().add(vo);
                }
            }
        }
        return new ArrayList<>(parents.values());
    }

    /**
     * 카테고리 추가. 실패하면 이유(메시지 코드)를, 성공하면 null을 돌려준다.
     */
    public String cate_insert(String cate_name, Integer cate_prtcode) {
        if (cate_name == null || cate_name.trim().isEmpty()) {
            return "emptyName";
        }
        if (adminCategoryMapper.getSameNameCount(cate_name.trim(), cate_prtcode) > 0) {
            return "duplicateName";
        }
        adminCategoryMapper.cate_insert(cate_name.trim(), cate_prtcode);
        return null;
    }

    public String cate_update(Integer cate_code, String cate_name) {
        if (cate_name == null || cate_name.trim().isEmpty()) {
            return "emptyName";
        }
        adminCategoryMapper.cate_update(cate_code, cate_name.trim());
        return null;
    }

    /**
     * 카테고리 삭제. 하위 카테고리나 상품이 남아 있으면 삭제하지 않는다.
     * (상품이 참조하는 카테고리를 지우면 상품 화면에서 오류가 나기 때문)
     */
    public String cate_delete(Integer cate_code) {
        if (adminCategoryMapper.getChildCount(cate_code) > 0) {
            return "hasChildren";
        }
        if (adminCategoryMapper.getProductCount(cate_code) > 0) {
            return "hasProducts";
        }
        adminCategoryMapper.cate_delete(cate_code);
        return null;
    }
}
