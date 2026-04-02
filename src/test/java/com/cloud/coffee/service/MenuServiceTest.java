package com.cloud.coffee.service;

import com.cloud.coffee.domain.Menu;
import com.cloud.coffee.domain.OrderItem;
import com.cloud.coffee.domain.Orders;
import com.cloud.coffee.dto.TopMenuResponse;
import com.cloud.coffee.repository.MenuRepository;
import com.cloud.coffee.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("전체 메뉴 목록 조회 성공")
    void getAllMenus_success() {
        menuRepository.deleteAll();

        menuRepository.save(new Menu("아메리카노", 3000L));
        menuRepository.save(new Menu("카페라떼", 4000L));

        List<Menu> menus = menuService.getAllMenus();

        assertThat(menus).hasSize(2);

        assertThat(menus)
                .extracting("name")
                .containsExactlyInAnyOrder("아메리카노", "카페라떼");
    }

    @Test
    @DisplayName("인기 메뉴 조회 성공 - 많이 팔린 순서대로 정렬되어야 한다")
    void getTopMenus_success() {
        orderRepository.deleteAll();
        menuRepository.deleteAll();

        Menu americano = menuRepository.save(new Menu("아메리카노", 3000L));
        Menu latte = menuRepository.save(new Menu("카페라떼", 4000L));

        Orders order1 = new Orders(1L, 3000L);
        order1.addOrderItem(new OrderItem(americano.getId(), americano.getName(), americano.getPrice(), 1L));
        orderRepository.save(order1);

        Orders order2 = new Orders(1L, 12000L);
        order2.addOrderItem(new OrderItem(latte.getId(), latte.getName(), latte.getPrice(), 3L));
        orderRepository.save(order2);

        List<TopMenuResponse> topMenus = menuService.getTopMenus();

        assertThat(topMenus).hasSize(2);

        assertThat(topMenus.get(0).getMenuName()).isEqualTo("카페라떼");
        assertThat(topMenus.get(0).getTotalQuantity()).isEqualTo(3L);

        assertThat(topMenus.get(1).getMenuName()).isEqualTo("아메리카노");
        assertThat(topMenus.get(1).getTotalQuantity()).isEqualTo(1L);
    }
}