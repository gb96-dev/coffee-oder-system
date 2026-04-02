package com.cloud.coffee.controller;

import com.cloud.coffee.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        Long orderId = orderService.order(request.getUserId(), request.getMenuId(), request.getQuantity());
        return ResponseEntity.ok("주문이 성공적으로 완료되었습니다. 주문 ID: " + orderId);
    }
}