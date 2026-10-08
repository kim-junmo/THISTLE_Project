package com.docmall.thistle.kakaopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoPayService {

    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${approval}")
    private String approval;

    @Value("${cancel}")
    private String cancel;

    @Value("${fail}")
    private String fail;

    // tid, 주문번호를 필드에 저장하지 않는다. (서비스도 싱글톤이라 사용자끼리 값이 섞임)
    // ready()가 돌려준 tid를 컨트롤러가 세션에 보관했다가 approve()에 넘겨준다.

    public ReadyResponse ready(String partnerOrderId, String partnerUserId, String itemName, int quantity, int totalAmount, int taxFreeAmount, int vatAmount) {
        // Request header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.set("Content-Type", "Application/json;charset=utf-8");
//        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ReadyRequest readyRequest = ReadyRequest.builder()
                .cid(cid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .itemName(itemName)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .taxFreeAmount(taxFreeAmount)
                .vatAmount(vatAmount)
                .approvalUrl(approval) // 성공.  카카오페이 섭에서 이주소를 찾아온다.
                .cancelUrl(cancel) // 취소
                .failUrl(fail)  // 실패.
                .build();

        // Send reqeust
        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                ReadyResponse.class
        );
        ReadyResponse readyResponse = response.getBody();

        return readyResponse;
    }

    // 2)결제승인요청(approve)
    public String approve(String pgToken, String tid, String partnerOrderId, String partnerUserId) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .pgToken(pgToken)
                .build();

        // Send Request
        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class
            );

            // 승인 결과를 저장한다.
            // save the result of approval
            String approveResponse = response.getBody();
            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAsString();
        }
    }

}
