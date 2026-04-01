package com.cloud.coffee.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.cloud.coffee.domain.User;
import com.cloud.coffee.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 저장 테스트 - 초기 포인트가 정상적으로 저장된다.")
    void insertDummyUser() {
        userRepository.deleteAll();
        User testUser = new User(10000L);
        userRepository.save(testUser);
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getPoint()).isEqualTo(10000L);

        System.out.println("=== 💡 DB에 유저 데이터가 성공적으로 들어갔습니다! ===");
    }
}