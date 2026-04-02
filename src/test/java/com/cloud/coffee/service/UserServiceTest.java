package com.cloud.coffee.service;

import com.cloud.coffee.domain.User;
import com.cloud.coffee.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("포인트 충전 성공")
    void chargePoint_success() {
        User user = userRepository.save(new User(0L));
        Long chargeAmount = 10000L;

        userService.chargePoint(user.getId(), chargeAmount);

        Long currentPoint = userService.getPoint(user.getId());
        assertThat(currentPoint).isEqualTo(10000L);
    }
}