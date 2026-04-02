package com.cloud.coffee.service;

import com.cloud.coffee.domain.Menu;
import com.cloud.coffee.domain.OrderItem;
import com.cloud.coffee.domain.Orders;
import com.cloud.coffee.domain.User;
import com.cloud.coffee.repository.MenuRepository;
import com.cloud.coffee.repository.OrderRepository;
import com.cloud.coffee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    private final MockDataPlatformSender dataPlatformSender;

    @Transactional
    public Long order(Long userId, Long menuId, Long quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        Long totalPrice = menu.getPrice() * quantity;

        user.deductPoint(totalPrice);

        Orders order = new Orders(userId, totalPrice);

        OrderItem orderItem = new OrderItem(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                quantity
        );

        order.addOrderItem(orderItem);

        Orders savedOrder = orderRepository.save(order);

        dataPlatformSender.sendOrderData(userId, menuId, totalPrice);

        return savedOrder.getId();
    }
}