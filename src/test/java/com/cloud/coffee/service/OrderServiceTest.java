package com.cloud.coffee.service;

import com.cloud.coffee.domain.Menu;
import com.cloud.coffee.domain.Orders;
import com.cloud.coffee.domain.User;
import com.cloud.coffee.repository.MenuRepository;
import com.cloud.coffee.repository.OrderRepository;
import com.cloud.coffee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private UserRepository userRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private OrderRepository orderRepository;

    private User savedUser;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        menuRepository.deleteAll();

        // Given: 기본 유저(포인트 10,000원)와 메뉴(아메리카노 3,000원) 세팅
        savedUser = userRepository.save(new User(10000L));
        savedMenu = menuRepository.save(new Menu("아메리카노", 3000L));
    }

    @Test
    @DisplayName("커피 주문 성공 - 포인트 차감 및 주문 내역 생성 확인")
    void order_success() {
        Long orderId = orderService.order(savedUser.getId(), savedMenu.getId(), 2L);

        Orders order = orderRepository.findById(orderId).orElseThrow();
        assertThat(order.getTotalPrice()).isEqualTo(6000L);

        User user = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(user.getPoint()).isEqualTo(4000L);
    }

    @Test
    @DisplayName("커피 주문 실패 - 잔액 부족 시 예외 발생")
    void order_fail_not_enough_point() {
        assertThatThrownBy(() -> {
            orderService.order(savedUser.getId(), savedMenu.getId(), 4L);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잔액이 부족합니다.");
    }
}