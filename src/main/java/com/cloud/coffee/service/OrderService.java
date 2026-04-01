package com.cloud.coffee.service;

import com.cloud.coffee.domain.Menu;
import com.cloud.coffee.domain.OrderItem;
import com.cloud.coffee.domain.Orders;
import com.cloud.coffee.domain.User;
import com.cloud.coffee.repository.MenuRepository;
import com.cloud.coffee.repository.OrderItemRepository;
import com.cloud.coffee.repository.OrderRepository;
import com.cloud.coffee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 커피 주문하기
     */
    @Transactional
    public Long createOrder(Long userId, Long menuId, int quantity) {
        // 1. 유저 조회 (동시성 제어를 위한 비관적 락 적용)
        User user = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 2. 메뉴 조회
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        // 3. 총 결제 금액 계산
        Long totalPrice = menu.getPrice() * quantity;

        // 4. 유저 잔액 차감 (User 엔티티 내부에 차감 로직을 만들 예정입니다)
        user.deductPoint(totalPrice);

        // 5. 주문(Orders) 생성 및 저장
        Orders order = new Orders(user);
        Orders savedOrder = orderRepository.save(order);

        // 6. 주문 상세(OrderItem) 생성 및 저장
        OrderItem orderItem = new OrderItem(savedOrder, menu, quantity, menu.getPrice());
        orderItemRepository.save(orderItem);

        return savedOrder.getId();
    }
}