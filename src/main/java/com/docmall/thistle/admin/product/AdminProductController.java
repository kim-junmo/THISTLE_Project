package com.docmall.thistle.admin.product;

import com.docmall.thistle.common.category.CategoryService;
import com.docmall.thistle.common.category.CategoryVO;
import com.docmall.thistle.common.constants.Constants;
import com.docmall.thistle.common.dto.Criteria;
import com.docmall.thistle.common.dto.PageDTO;
import com.docmall.thistle.common.util.FileManagerUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    //ckeditor 상품 설명 이미지 업로드
    //MultipartFile upload : ckeditor 업로드 탭에서 나온 파일 첨부 태그 파라미터(<input type="file" name="upload">)를 참조함.
    //MultipartFile upload : 첨부된 파일정보를 가지고 있음.
    //HttpServletRequest request : 클라이언트의 요청정보를 가지고 있는 객체.
    //HttpServletResponse response : 서버에서 클라이언트에게 보낸 정보를 응답하는 객체.
    @PostMapping("/imageupload")
    public void imageupload(HttpServletRequest request, HttpServletResponse response, MultipartFile upload) {

        //입출력 스트림으로 OutputStream을 사용함. 자바의 입출력 스트림 공부해서 정리할 필요가 있음.
        OutputStream out = null;
        //PrintWriter: 서버에서 클라이언트에게 응답정보를 보낼때 사용.(업로드한 이미지 정보를 클라이언트(브라우저)에게 보내는 작업용도)
        PrintWriter printWriter = null;

        try {
            //1)ckeditor를 통한 파일 업로드 처리 기능.
            String OriginalfileName = upload.getOriginalFilename(); //업로드 할 클라이언트 파일 이름.
            String fileName = URLEncoder.encode(OriginalfileName, StandardCharsets.UTF_8.toString());
            byte[] bytes = upload.getBytes(); //업로드 할 파일의 바이트 배열

            if(!uploadCKPath.endsWith("/") && !uploadCKPath.endsWith("\\")) {
                uploadCKPath += System.getProperty("file.separator");
            }

            //uploadCKPath + fileName : "C:\\dev\\upload\\ckeditor\\" + "fileName(ex. abc.gif)
            //그렇기 때문에 경로에 \\가 들어가 있어야 함. 없으면 C:\\dev\\upload\\ckeditorabc.gif 이렇게 됨.
            String ckUploadPath = uploadCKPath + fileName;

            //C:\\dev\\upload\\ckeditor\\abc.gif 이 한줄에 의해 파일이 생성이 된다.
            //이때 파일은 0byte로 생성이 된다.
            out = new FileOutputStream(ckUploadPath);

            out.write(bytes); //빨때(스트림)의 공간에 업로드할 파일의 바이트 배열을 채운 상태.
            out.flush(); //0byte의 파일의 크기가 채워진 정상적인 파일로 인식이 된다.

            //2)업로드한 이미지 파일에 대한 정보를 클라이언트에게 보내는 작업.
            printWriter = response.getWriter();

            //이메일 첨부 시 이미지가 깨지는 문제 발생, 상수 Constants에 도메인 주소를 넣어 사용하면 깨지지 않음.
            String fileUrl = Constants.ROOT_URL + "/admin/product/display/" + fileName; //매핑주소/이미지파일명
            //String fileUrl = fileName;

            //ckeditor 4.1.2에서는 파일정보를 다음과 같이 구성하여 보내야 한다.
            //{"fileName" : "abc.gif", "uploaded":1, "url":"/ckupload/abc.gif"} : 자바스트의 제이슨 문법 스타일.
            //{"fileName" : "변수", "uploaded":1, "url":"변수"}
            //즉 변수 값을 포함하는 JSON 문자열을 생성하고 출력하는 예제이다. 스트림에 내용을 채움.
            printWriter.println("{\"fileName\" :\"" + OriginalfileName + "\", \"uploaded\":1,\"url\":\"" + fileUrl + "\"}"); //스프림에 채움.
            printWriter.flush(); //println의 정보가 브라우저로 보내진다.

        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if(out != null) {
                try {
                    out.close();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            if(printWriter != null) printWriter.close();
        }
    }

    //<img src="매핑주소">를 통해 이미지를 제공하는 핸들러
    @GetMapping("/display/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable("fileName") String fileName) {
        log.info("파일이미지: " + fileName);

        ResponseEntity<byte[]> entity = null;

        try {
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());
            File file = new File(uploadCKPath + decodedFileName);

            if (!file.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());


            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file.toPath()));
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + decodedFileName + "\"");

            entity = new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return entity;
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
