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

    @GetMapping("/balance")
    public ResponseEntity<Long> getBalance(@RequestParam("userId") Long userId) {
        Long balance = userService.getPoint(userId);
        return ResponseEntity.ok(balance);
    }

     @PostMapping("/charge")
    public ResponseEntity<String> chargePoint(
            @RequestParam("userId") Long userId,
            @RequestParam("amount") Long amount) {

        Long updatedPoint = userService.chargePoint(userId, amount);
        return ResponseEntity.ok("포인트 충전이 완료되었습니다. 현재 잔액: " + updatedPoint + "원");
    }
}