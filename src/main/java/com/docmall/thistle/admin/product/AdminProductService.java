package com.docmall.thistle.admin.product;

import com.docmall.thistle.common.dto.Criteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminProductService {

    private final AdminProductMapper adminProductMapper;

    public void pro_insert(ProductVO vo) {
        adminProductMapper.pro_insert(vo);
    }

    public List<ProductVO> pro_list(Criteria cri) {
        try {
            log.info("Criteria in pro_list: {}", cri);
            return adminProductMapper.pro_list(cri);
        } catch (Exception e) {
            log.error("Error in pro_list: ", e);
            throw e;
        }
    }

    public int getTotalCount(Criteria cri) {
        return adminProductMapper.getTotalCount(cri);
    }

    public ProductVO pro_edit(Integer pro_num) {
        return adminProductMapper.pro_edit(pro_num);
    }

    public void pro_edit_OK(ProductVO vo) {
        adminProductMapper.pro_edit_OK(vo);
    }

    public void pro_delete(Integer pro_num) {
        adminProductMapper.pro_delete(pro_num);
    }

    public void pro_checked_modify(List<Integer> pro_num_arr, List<Integer> pro_price_arr, List<String> pro_buy_arr) {

        List<ProductDTO> pro_modify_list = new ArrayList<>();

        for(int i=0; i<pro_num_arr.size(); i++) {
            ProductDTO productDTO = new ProductDTO(pro_num_arr.get(i), pro_price_arr.get(i), pro_buy_arr.get(i));
            pro_modify_list.add(productDTO);
        }

        adminProductMapper.pro_checked_modify(pro_modify_list);
    }
}
