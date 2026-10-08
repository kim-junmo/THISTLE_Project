package com.docmall.thistle.review;

import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.common.dto.PageDTO;
import com.docmall.thistle.user.UserVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review/*")
@Controller
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/revlist/{pro_num}/{page}")
    public ResponseEntity<Map<String, Object>> revlistOK(@PathVariable("pro_num") int pro_num, @PathVariable("page") int page) throws Exception {

        ResponseEntity<Map<String, Object>> entity = null;
        Map<String, Object> map = new HashMap<>();

        //후기 목록 작업
        Criteria cri = new Criteria();
        cri.setAmount(2);
        cri.setPageNum(page);

        List<ReviewVO> revlist = reviewService.rev_list(pro_num, cri);

        //페이징 정보
        int revcount = reviewService.getCountReviewByPro_num(pro_num);
        PageDTO pageMaker = new PageDTO(cri, revcount);

        map.put("pageMaker", pageMaker);
        map.put("revlist", revlist);

        entity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

        return entity;
    }

    @PostMapping(value = "/review_save", consumes = {"application/json"}, produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> review_saveOK(@RequestBody ReviewVO vo, HttpSession session) throws Exception {

        String user_id = ((UserVO) session.getAttribute("login_status")).getUser_id();
        vo.setUser_id(user_id);

        reviewService.review_save(vo);

        log.info("상품 후기 데이터 : " + vo);
        // 화면(pro_detail.html)에서 "success"를 확인하므로 반드시 응답 본문을 보내야 한다.
        ResponseEntity<String> entity = new ResponseEntity<String>("success", HttpStatus.OK);

        return entity;
    }

    @GetMapping("/review_modify/{re_code}")
    public ResponseEntity<ReviewVO> review_modifyForm(@PathVariable("re_code") Long re_code) throws Exception {

        ResponseEntity<ReviewVO> entity = null;

        entity = new ResponseEntity<ReviewVO>(reviewService.review_modify(re_code), HttpStatus.OK);

        return entity;
    }

    @PutMapping("/review_modify")
    public ResponseEntity<String> review_modifyOK(@RequestBody ReviewVO vo) throws Exception {

        ResponseEntity<String> entity = null;

        reviewService.review_update(vo);

        entity = new ResponseEntity<String>("success", HttpStatus.OK);

        return entity;
    }

    @DeleteMapping("/review_delete/{re_code}")
    public ResponseEntity<String> review_deleteOK(@PathVariable("re_code") Long re_code) throws Exception {
        ResponseEntity<String> entity = null;

        reviewService.review_delete(re_code);

        entity = new ResponseEntity<String>("success", HttpStatus.OK);

        return entity;
    }
}
