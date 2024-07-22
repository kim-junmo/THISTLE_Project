package com.docmall.thistle.product;

import com.docmall.thistle.admin.product.ProductVO;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.common.dto.PageDTO;
import com.docmall.thistle.common.util.FileManagerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequestMapping("/product/*")
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;

    //상품 이미지 경로
    @Value("${file.product.image.dir}")
    private String uploadPath;

    @GetMapping("/pro_list")
    public void pro_listForm(
            @ModelAttribute("cate_code") int cate_code,
            @ModelAttribute("cate_name") String cate_name,
            Criteria cri, Model model) throws Exception {

        log.info("2차 카테고리 코드 : " + cate_code);
        log.info("2차 카테고리 이름 : " + cate_name);

        List<ProductVO> pro_list = productService.pro_list(cate_code, cri);

        pro_list.forEach(vo -> {
            vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
        });
        int totalcount = productService.getCountProductByCategory(cate_code);

        model.addAttribute("pro_list", pro_list);
        model.addAttribute("pageMaker", new PageDTO(cri, totalcount));
    }

    @GetMapping("/image_display")
    public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

        return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
    }

    @GetMapping("/pro_info")
    public void pro_infoForm(int pro_num, Model model) throws Exception {

        ProductVO vo = productService.pro_into(pro_num);
        vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));

        model.addAttribute("product", vo);
    }

    @GetMapping("/pro_detail")
    public void pro_detailForm(int pro_num, Model model) throws Exception {

        ProductVO vo = productService.pro_into(pro_num);
        vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));

        model.addAttribute("product", vo);

    }


}
