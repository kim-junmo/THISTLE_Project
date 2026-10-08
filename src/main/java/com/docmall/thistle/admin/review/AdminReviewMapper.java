package com.docmall.thistle.admin.review;

import com.docmall.thistle.common.dto.Criteria;

import java.util.List;

public interface AdminReviewMapper {

    List<AdminReviewVO> review_list(Criteria cri);

    int getTotalCount(Criteria cri);

    void review_delete(Long re_code);
}
