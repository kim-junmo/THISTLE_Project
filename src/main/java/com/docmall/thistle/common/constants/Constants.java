package com.docmall.thistle.common.constants;

//클래스 앞에 final을 달면 상속 금지가 된다.
public class Constants {

    //메일 설정, 메일 템플릿 폴더
    public static final String MAILFOLDNAME = "email";

    //쇼핑몰 주문정보 리스트 출력개수
    public static final int MYSHOP_ORDER_LIST_AMOUNT= 2;

    //관리자 상품 리스트 출력개수
    public static final int ADMIN_PRODUCT_LIST_AMOUNT= 2;

    //관리자 주문 리스트 출력개수
    public static final int ADMIN_ORDER_LIST_AMOUNT= 2;

    //관리자 회원 리스트 출력개수
    public static final int ADMIN_USER_LIST_AMOUNT= 1;

    //관리자 메일 리스트 출력 개수
    public static final int ADMIN_MAILING_LIST_AMOUNT= 10;

    //메일 이미지 첨부 시 도메인 주소
    public static final String ROOT_URL = "http://localhost:9090";

    //관리자 리뷰 리스트 출력개수
    public static final int ADMIN_REVIEW_LIST_AMOUNT = 10;

    //주문 진행 상태(payinfo_tbl.p_status에 저장). 기존 값 '미납', '결제완료'를 그대로 포함한다.
    public static final java.util.List<String> ORDER_STATUS_LIST =
            java.util.List.of("미납", "결제완료", "배송준비중", "배송중", "배송완료", "주문취소");

    //매출 통계에서 제외할 상태
    public static final String ORDER_STATUS_CANCEL = "주문취소";
}
