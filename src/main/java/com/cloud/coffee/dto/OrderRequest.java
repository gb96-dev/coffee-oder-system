package com.cloud.coffee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private Long userId;
    private Long menuId;
    private Long quantity;
}