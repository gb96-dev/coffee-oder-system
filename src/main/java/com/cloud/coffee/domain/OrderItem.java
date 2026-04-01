package com.cloud.coffee.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Long quantity;

    private Long orderPrice;

    public OrderItem(Orders order, Menu menu, int quantity, Long price) {
        this.order = order;
        this.menu = menu;
        this.quantity = Long.valueOf(quantity);
        this.orderPrice = price;
    }
}