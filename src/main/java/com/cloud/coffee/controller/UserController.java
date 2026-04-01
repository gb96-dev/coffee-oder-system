package com.cloud.coffee.controller;

import com.cloud.coffee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. 사용자 잔액 조회
    @GetMapping("/balance")
    public ResponseEntity<Long> getBalance(@RequestParam("userId") Long userId) {
        Long balance = userService.getUserPoint(userId);
        return ResponseEntity.ok(balance);
    }

    // 2. 사용자 포인트 충전
    @PostMapping("/charge")
    public ResponseEntity<String> chargePoint(
            @RequestParam("userId") Long userId,
            @RequestParam("amount") Long amount) {

        userService.chargePoint(userId, amount);
        return ResponseEntity.ok("포인트 충전이 완료되었습니다.");
    }
}