package com.cloud.coffee.dto;

import lombok.Getter;

@Getter
public class TopMenuResponse {
    private Long menuId;
    private String menuName;
    private Long totalQuantity;

    public TopMenuResponse(Long menuId, String menuName, Long totalQuantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.totalQuantity = totalQuantity;
    }
}