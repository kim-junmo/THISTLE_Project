package com.docmall.thistle.admin.product;

import com.docmall.thistle.common.dto.Criteria;

import java.util.List;

public interface AdminProductMapper {

    void pro_insert(ProductVO vo);

    List<ProductVO> pro_list(Criteria cri);

    int getTotalCount(Criteria cri);

    ProductVO pro_edit(Integer pro_num);

    void pro_edit_OK(ProductVO vo);

    void pro_delete(Integer pro_num);

    void pro_checked_modify(List<ProductDTO> pro_modify_list);
}
