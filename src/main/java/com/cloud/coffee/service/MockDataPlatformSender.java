package com.cloud.coffee.service;

import org.springframework.stereotype.Component;

@Component
public class MockDataPlatformSender {

    public void sendOrderData(Long userId, Long menuId, Long amount) {
        System.out.println("=================================================");
        System.out.println("[데이터 플랫폼 전송] >> 사용자: " + userId + ", 메뉴: " + menuId + ", 결제금액: " + amount + "원 전송 완료");
        System.out.println("=================================================");
    }
}