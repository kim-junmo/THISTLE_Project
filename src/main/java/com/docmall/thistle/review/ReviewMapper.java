package com.docmall.thistle.review;

import com.docmall.thistle.common.dto.Criteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewMapper {

    List<ReviewVO> rev_list(@Param("pro_num") Integer pro_num, @Param("cri") Criteria cri);

    int getCountReviewByPro_num(Integer pro_num);

    void review_save(ReviewVO vo);

    ReviewVO review_modify(Long re_code);

    void review_update(ReviewVO vo);

    void review_delete(Long re_code);
}
