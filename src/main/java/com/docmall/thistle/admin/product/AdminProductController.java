package com.docmall.thistle.admin.product;

import com.docmall.thistle.common.category.CategoryService;
import com.docmall.thistle.common.category.CategoryVO;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.common.dto.PageDTO;
import com.docmall.thistle.common.util.FileManagerUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

@RequestMapping("/admin/product/*")
@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final CategoryService categoryService;

    //상품 이미지 경로
    @Value("${file.product.image.dir}")
    private String uploadPath;

    //ckeditor 파일 업로드 경로
    @Value("${file.ckdir}")
    private String uploadCKPath;


    //상품 등록 폼
    @GetMapping("pro_insert")
// "/pro_insert" URL에 대한 GET 요청을 처리하는 메서드를 정의합니다.

    public void pro_insertForm(Model model) {
// 상품 등록 폼을 표시하는 메서드입니다. Model 객체를 매개변수로 받습니다.

        List<CategoryVO> cate_list = categoryService.getFirstCategoryList();
        // 1차 카테고리 목록을 조회합니다.

        model.addAttribute("cate_list", cate_list);
        // 조회한 카테고리 목록을 모델에 추가합니다.
    }

    @PostMapping("/pro_insert")
// "/pro_insert" URL에 대한 POST 요청을 처리하는 메서드를 정의합니다.

    public String pro_insertOK(ProductVO vo, MultipartFile uploadFile) throws Exception {
// 상품을 등록하는 메서드입니다. 상품 정보와 업로드된 파일을 매개변수로 받습니다.

        String dateFolder = FileManagerUtils.getDateFolder();
        // 날짜 기반의 폴더 이름을 생성합니다.

        String saveFileName = FileManagerUtils.uploadFile(uploadPath, dateFolder, uploadFile);
        // 파일을 업로드하고 저장된 파일 이름을 받아옵니다.

        vo.setPro_img(saveFileName);
        vo.setPro_up_folder(dateFolder);
        // 상품 정보에 파일 이름과 업로드 폴더 정보를 설정합니다.

        log.info("상품정보 : " + vo);
        // 상품 정보를 로그에 출력합니다.

        adminProductService.pro_insert(vo);
        // 상품 정보를 데이터베이스에 저장합니다.

        return "redirect:/admin/product/pro_list";
        // 상품 목록 페이지로 리다이렉트합니다.
    }

    @PostMapping("/imageupload")
// "/imageupload" URL에 대한 POST 요청을 처리하는 메서드를 정의합니다.

    public void imageuploadOK(HttpServletRequest request, HttpServletResponse response, MultipartFile upload) throws Exception {
// CKEditor의 이미지 업로드 요청을 처리하는 메서드입니다.

        OutputStream out = null;
        PrintWriter printWriter = null;
        // OutputStream과 PrintWriter 객체를 선언합니다.

        try {
            String fileName = upload.getOriginalFilename();
            // 업로드된 파일의 원본 이름을 가져옵니다.

            byte[] bytes = upload.getBytes();
            // 파일 데이터를 바이트 배열로 가져옵니다.

            String ckUploadPath = uploadCKPath + fileName;
            // 파일이 저장될 전체 경로를 생성합니다.

            out = new FileOutputStream(ckUploadPath);
            // 파일을 쓰기 위한 OutputStream을 생성합니다.

            out.write(bytes);
            out.flush();
            // 파일 데이터를 쓰고 버퍼를 비웁니다.

            printWriter = response.getWriter();
            // 응답을 위한 PrintWriter를 가져옵니다.

            String fileUrl = "/admin/product/display/" + fileName;
            // 업로드된 파일의 URL을 생성합니다.

            printWriter.println("{\"fileName\" :\"" + fileName + "\", \"uploaded\":1,\"url\":\"" + fileUrl + "\"}");
            printWriter.flush();
            // CKEditor에서 요구하는 형식의 JSON 응답을 생성하고 전송합니다.

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if(printWriter != null) printWriter.close();
            // 사용한 리소스를 닫습니다.
        }
    }

    @GetMapping("/display/{fileName}")
// "/display/{fileName}" URL에 대한 GET 요청을 처리하는 메서드를 정의합니다.

    public ResponseEntity<byte[]> getFile(@PathVariable("fileName") String fileName) {
// 파일을 표시하는 메서드입니다. 파일 이름을 경로 변수로 받습니다.

        log.info("파일이미지:" + fileName);
        // 파일 이름을 로그에 출력합니다.

        ResponseEntity<byte[]> entity = null;
        // ResponseEntity 객체를 선언합니다.

        try {
            entity = FileManagerUtils.getFile(uploadCKPath, fileName);
            // FileManagerUtils를 사용하여 파일을 가져옵니다.
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return entity;
        // ResponseEntity를 반환합니다.
    }

    @GetMapping("/pro_list")
// "/pro_list" URL에 대한 GET 요청을 처리하는 메서드를 정의합니다.

    public void pro_listForm(Criteria cri, Model model) throws Exception {
// 상품 목록을 표시하는 메서드입니다. Criteria 객체와 Model 객체를 매개변수로 받습니다.

//        cri.setAmount(2);
// 페이지당 표시할 항목 수를 설정하는 코드입니다. 현재는 주석 처리되어 있습니다.

        List<ProductVO> pro_list = adminProductService.pro_list(cri);
        // 주어진 Criteria에 따라 상품 목록을 조회합니다.

        pro_list.forEach(vo -> {
            vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
        });
        // 각 상품의 업로드 폴더 경로에서 백슬래시를 슬래시로 변경합니다.

        int totalCount = adminProductService.getTotalCount(cri);
        // 전체 상품 수를 조회합니다.

        model.addAttribute("pro_list", pro_list);
        model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
        // 상품 목록과 페이지 정보를 모델에 추가합니다.
    }

    @GetMapping("/image_display")
// "/image_display" URL에 대한 GET 요청을 처리하는 메서드를 정의합니다.

    public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
        return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
    }
// 이미지 파일을 표시하기 위한 메서드입니다. 파일 경로와 이름을 받아 파일 데이터를 반환합니다.

    @GetMapping("/pro_edit")
// "/pro_edit" URL에 대한 GET 요청을 처리하는 메서드를 정의합니다.

    public void pro_editForm(@ModelAttribute("cri") Criteria cri, Integer pro_num, Model model) throws Exception {
// 상품 수정 폼을 표시하는 메서드입니다. Criteria, 상품 번호, Model을 매개변수로 받습니다.

        List<CategoryVO> cate_list = categoryService.getFirstCategoryList();
        model.addAttribute("cate_list", cate_list);
        // 1차 카테고리 목록을 조회하여 모델에 추가합니다.

        ProductVO vo = adminProductService.pro_edit(pro_num);
        // 주어진 상품 번호로 상품 정보를 조회합니다.

        vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
        model.addAttribute("productVO", vo);
        // 상품 정보의 업로드 폴더 경로를 수정하고 모델에 추가합니다.

        int cate_code = vo.getCate_code();
        int cate_prtcode = categoryService.getFirstCategoryBySecondCategory(cate_code).getCate_prtcode();
        model.addAttribute("cate_prtcode", cate_prtcode);
        // 2차 카테고리 코드로 1차 카테고리 코드를 조회하여 모델에 추가합니다.

        model.addAttribute("sub_cate_list", categoryService.getSecondCategoryList(cate_prtcode));
        // 2차 카테고리 목록을 조회하여 모델에 추가합니다.
    }

    @PostMapping("/pro_edit")
    // "/pro_edit" URL에 대한 POST 요청을 처리하는 메서드를 정의합니다.

    public String pro_editOK(ProductVO vo, MultipartFile uploadFile, Criteria cri, RedirectAttributes rttr) throws Exception {
    // 상품 정보를 수정하는 메서드입니다. 상품 정보, 업로드 파일, Criteria, RedirectAttributes를 매개변수로 받습니다.

        log.info("상품수령정보 : " + vo);
        // 상품 정보를 로그에 출력합니다.

        if(!uploadFile.isEmpty()) {
            FileManagerUtils.delete(uploadPath, vo.getPro_up_folder(), vo.getPro_img(), "image");
            // 새 이미지가 업로드된 경우, 기존 이미지를 삭제합니다.

            String dateFolder = FileManagerUtils.getDateFolder();
            String saveFileName = FileManagerUtils.uploadFile(uploadPath, dateFolder, uploadFile);
            // 새 이미지를 업로드하고 파일 이름을 저장합니다.

            vo.setPro_img(saveFileName);
            vo.setPro_up_folder(dateFolder);
            // 상품 정보에 새 이미지 정보를 설정합니다.
        }
        log.info("상품 수정 정보: " + vo);
        // 수정된 상품 정보를 로그에 출력합니다.

        adminProductService.pro_edit_OK(vo);
        // 수정된 상품 정보를 데이터베이스에 저장합니다.

        return "redirect:/admin/product/pro_list" + cri.getListLink();
        // 상품 목록 페이지로 리다이렉트합니다. 페이지 정보를 URL에 포함시킵니다.
    }

    @PostMapping("/pro_delete")
// "/pro_delete" URL에 대한 POST 요청을 처리하는 메서드를 정의합니다.

    public String pro_deleteOK(Criteria cri, Integer pro_num) throws Exception {
// 상품 삭제를 처리하는 메서드입니다. Criteria와 상품 번호를 매개변수로 받습니다.

        adminProductService.pro_delete(pro_num);
        // adminProductService를 통해 지정된 상품 번호의 상품을 삭제합니다.

        return "redirect:/admin/product/pro_list" + cri.getListLink();
        // 상품 목록 페이지로 리다이렉트합니다. cri.getListLink()를 통해 현재 페이지 정보를 유지합니다.
    }

    @PostMapping("/pro_checked_modify")
// "/pro_checked_modify" URL에 대한 POST 요청을 처리하는 메서드를 정의합니다.

    public ResponseEntity<String> pro_checked_modify(
            @RequestParam("pro_num_arr") List<Integer> pro_num_arr,
            @RequestParam("pro_price_arr") List<Integer> pro_price_arr,
            @RequestParam("pro_buy_arr") List<String> pro_buy_arr) throws Exception {
// 체크된 여러 상품의 정보를 일괄 수정하는 메서드입니다. 상품 번호, 가격, 구매 가능 여부 리스트를 매개변수로 받습니다.

        adminProductService.pro_checked_modify(pro_num_arr, pro_price_arr, pro_buy_arr);
        // adminProductService를 통해 체크된 상품들의 정보를 일괄 수정합니다.

        ResponseEntity<String> entity = null;
        entity = new ResponseEntity<>("success", HttpStatus.OK);
        // 성공 응답을 생성합니다. "success" 메시지와 함께 HTTP 상태 코드 200 OK를 반환합니다.

        return entity;
        // 생성된 ResponseEntity를 반환합니다.
    }
}
