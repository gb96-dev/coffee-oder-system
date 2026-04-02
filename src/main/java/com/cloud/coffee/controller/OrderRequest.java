package com.cloud.coffee.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private Long userId;
    private Long menuId;
    private Long quantity;
}