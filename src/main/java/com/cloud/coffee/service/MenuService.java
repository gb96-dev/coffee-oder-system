package com.cloud.coffee.service;

import com.cloud.coffee.domain.Menu;
import com.cloud.coffee.dto.TopMenuResponse;
import com.cloud.coffee.repository.MenuRepository;
import com.cloud.coffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
    public List<TopMenuResponse> getTopMenus() {
        // 현재 시간 기준으로 정확히 3일 전 시간을 구합니다.
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);

        // 상위 3개만 끊어오기 위해 PageRequest를 사용합니다.
        org.springframework.data.domain.Pageable limitThree = org.springframework.data.domain.PageRequest.of(0, 3);

        List<Object[]> results = orderRepository.findTopMenus(startDate, limitThree);

        // Object[] 배열로 나온 결과를 보기 좋게 DTO 리스트로 변환합니다.
        return results.stream()
                .map(result -> new TopMenuResponse(
                        (Long) result[0],       // menuId
                        (String) result[1],     // menuName
                        (Long) result[2]        // totalQuantity
                ))
                .toList();
    }
}