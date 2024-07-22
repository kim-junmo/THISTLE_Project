package com.docmall.thistle.product;

import com.docmall.thistle.admin.product.ProductVO;
import com.docmall.thistle.common.dto.Criteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {

    List<ProductVO> pro_list(@Param("cate_code") int cate_code, @Param("cri") Criteria cri);

    int getCountProductByCategory(int cate_code);

    ProductVO pro_into(int pro_num);
}
