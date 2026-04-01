package com.cloud.coffee.service;

import com.cloud.coffee.domain.Menu;
import com.cloud.coffee.domain.User;
import com.cloud.coffee.repository.MenuRepository;
import com.cloud.coffee.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("커피 주문 성공 - 잔액 차감 확인")
    void createOrder_success() {
        // given: 10,000원을 가진 유저와 3,000원짜리 메뉴가 준비되었을 때
        User user = userRepository.save(new User(10000L));
        Menu menu = menuRepository.save(new Menu("아메리카노", 3000L));
        int quantity = 1;

        // when: 아메리카노 1잔을 주문하면
        orderService.createOrder(user.getId(), menu.getId(), quantity);

        // then: 유저의 남은 포인트는 7,000원이어야 한다.
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getPoint()).isEqualTo(7000L);
    }
}