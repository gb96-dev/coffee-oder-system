package com.cloud.coffee.service;

import com.cloud.coffee.domain.User;
import jakarta.persistence.*;
import com.cloud.coffee.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트 완료 후 DB를 자동으로 롤백해 줍니다.
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("포인트 충전 성공")
    void chargePoint_success() {
        // given: 0포인트를 가진 유저가 준비되었을 때
        User user = userRepository.save(new User(0L));
        Long chargeAmount = 10000L;

        // when: 10,000원을 충전하면
        userService.chargePoint(user.getId(), chargeAmount);

        // then: 유저의 최종 포인트는 10,000원이어야 한다.
        Long currentPoint = userService.getPoint(user.getId());
        assertThat(currentPoint).isEqualTo(10000L);
    }
}