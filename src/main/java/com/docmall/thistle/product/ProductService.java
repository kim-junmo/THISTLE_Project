package com.docmall.thistle.product;

import com.docmall.thistle.admin.product.ProductVO;
import com.docmall.thistle.common.dto.Criteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductMapper productMapper;

    public List<ProductVO> pro_list(int cate_code, Criteria cri) {
        return productMapper.pro_list(cate_code, cri);
    }

    public int getCountProductByCategory(int cate_code) {
        return productMapper.getCountProductByCategory(cate_code);
    }

    public ProductVO pro_into(int pro_num) {
        return productMapper.pro_into(pro_num);
    }


}
