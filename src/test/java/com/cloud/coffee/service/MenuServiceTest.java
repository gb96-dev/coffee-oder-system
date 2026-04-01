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
@Transactional // 테스트가 끝나면 여기서 넣은 가짜 데이터는 롤백됩니다.
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("전체 메뉴 목록 조회 성공")
    void getAllMenus_success() {
        // 💡 [치트키] 테스트 시작 전, 기존에 DB에 있던 메뉴 데이터를 싹 지워줍니다.
        menuRepository.deleteAll();

        // given: 테스트용 가짜 데이터 2개를 새로 넣습니다.
        menuRepository.save(new Menu("아메리카노", 3000L));
        menuRepository.save(new Menu("카페라떼", 4000L));

        // when: 전체 메뉴를 조회하면
        List<Menu> menus = menuService.getAllMenus();

        // then: 1. 개수가 정확히 2개여야 합니다.
        assertThat(menus).hasSize(2);

        // then: 2. 순서와 상관없이 "아메리카노"와 "카페라떼"가 포함되어 있어야 합니다.
        assertThat(menus)
                .extracting("name")
                .containsExactlyInAnyOrder("아메리카노", "카페라떼");
    }
}