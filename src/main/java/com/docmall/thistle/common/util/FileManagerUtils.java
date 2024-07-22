package com.docmall.thistle.common.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileManagerUtils {
    // 파일 관리 유틸리티 클래스 선언

    public static String getDateFolder() {
        // 현재 날짜를 기반으로 폴더 이름을 생성하는 메서드
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 형식 지정
        Date date = new Date(); // 현재 날짜 가져오기
        String str = sdf.format(date); // 지정된 형식으로 날짜 포맷팅
        return str.replace("-", File.separator); // 날짜 구분자를 시스템 파일 구분자로 변경
    }

    public static String uploadFile(String uploadFolder, String dateFolder, MultipartFile uploadFile) {
        // 파일 업로드를 처리하는 메서드
        String realUploadFileName = ""; // 실제 업로드될 파일 이름 초기화
        File file = new File(uploadFolder, dateFolder); // 업로드 경로 설정
        if(file.exists() == false) {
            file.mkdirs(); // 업로드 경로가 없으면 생성
        }
        String ClientFileName = uploadFile.getOriginalFilename(); // 원본 파일 이름 가져오기
        UUID uuid = UUID.randomUUID(); // 고유 식별자 생성
        realUploadFileName = uuid.toString() + "." + ClientFileName; // 고유한 파일 이름 생성

        try {
            File saveFile = new File(file, realUploadFileName); // 저장할 파일 객체 생성
            uploadFile.transferTo(saveFile); // 파일 저장

            if(CheckImageType(saveFile)) { // 이미지 파일인지 확인
                File thumbFile = new File(file, "S_" + realUploadFileName); // 썸네일 파일 객체 생성
                BufferedImage bo_img = ImageIO.read(saveFile); // 원본 이미지 읽기
                int thumbWidth = 300; // 썸네일 너비 설정
                int thumbHeight = 450; // 비율에 맞춘 썸네일 높이 계산

                Thumbnails.of(saveFile)
                        .size(thumbWidth, thumbHeight)
                        .toFile(thumbFile); // 썸네일 생성
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생시 스택 트레이스 출력
        }
        return realUploadFileName; // 실제 업로드된 파일 이름 반환
    }

    public static boolean CheckImageType(File saveFile) {
        // 파일이 이미지 타입인지 확인하는 메서드
        boolean isImageType = false; // 이미지 타입 여부 초기화
        try {
            String contextType = Files.probeContentType(saveFile.toPath()); // 파일의 MIME 타입 확인
            isImageType = contextType.startsWith("image"); // 이미지 타입인지 확인
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생시 스택 트레이스 출력
        }
        return isImageType; // 이미지 타입 여부 반환
    }

    public static ResponseEntity<byte[]> getFile(String uploadPath, String fileName) throws Exception {
        // 파일을 다운로드하기 위한 ResponseEntity를 생성하는 메서드
        ResponseEntity<byte[]> entity = null; // ResponseEntity 초기화
        File file = new File(uploadPath, fileName); // 파일 객체 생성
        if(!file.exists()) {
            return entity; // 파일이 존재하지 않으면 null 반환
        }
        HttpHeaders headers = new HttpHeaders(); // HTTP 헤더 생성
        headers.add("Content-Type", Files.probeContentType(file.toPath())); // 컨텐츠 타입 설정
        entity = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK); // ResponseEntity 생성
        return entity; // ResponseEntity 반환
    }

    public static void delete(String uploadPath, String dateFolderName, String fileName, String type) throws Exception {
        // 파일을 삭제하는 메서드
        File file2 = new File((uploadPath + "\\" + dateFolderName + "\\" + fileName.substring(2)).replace('\\', File.separatorChar));
        // 원본 파일 경로 생성
        if(file2.exists()) file2.delete(); // 원본 파일 존재시 삭제

        if(type.equals("image")) { // 이미지 파일인 경우
            File file1 = new File((uploadPath + "\\" + dateFolderName + "\\" + fileName).replace('\\', File.separatorChar));
            // 썸네일 파일 경로 생성
            if(file1.exists()) file1.delete(); // 썸네일 파일 존재시 삭제
        }
    }
}
