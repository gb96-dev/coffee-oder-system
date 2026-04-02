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
    private Orders orders;

    private Long menuId;

    private String menuName;

    private Long orderPrice;

    private Long quantity;

    public OrderItem(Long menuId, String menuName, Long orderPrice, Long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}