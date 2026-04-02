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
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);

                org.springframework.data.domain.Pageable limitThree = org.springframework.data.domain.PageRequest.of(0, 3);

        List<Object[]> results = orderRepository.findTopMenus(startDate, limitThree);

        return results.stream()
                .map(result -> new TopMenuResponse(
                        (Long) result[0],       // menuId
                        (String) result[1],     // menuName
                        (Long) result[2]        // totalQuantity
                ))
                .toList();
    }
}