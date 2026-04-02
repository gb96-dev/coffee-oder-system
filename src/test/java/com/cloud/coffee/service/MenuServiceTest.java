package com.cloud.coffee.service;

import com.cloud.coffee.domain.Menu;
import com.cloud.coffee.repository.MenuRepository;
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
}