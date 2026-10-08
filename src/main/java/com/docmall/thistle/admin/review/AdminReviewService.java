package com.docmall.thistle.admin.review;

import com.docmall.thistle.common.dto.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminReviewService {

    private final AdminReviewMapper adminReviewMapper;

    public List<AdminReviewVO> review_list(Criteria cri) {
        return adminReviewMapper.review_list(cri);
    }

    public int getTotalCount(Criteria cri) {
        return adminReviewMapper.getTotalCount(cri);
    }

    public void review_delete(Long re_code) {
        adminReviewMapper.review_delete(re_code);
    }
}
