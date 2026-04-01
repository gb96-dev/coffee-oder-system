package com.cloud.coffee.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long point;

    public User(Long point) {
        this.point = point;
    }

    public void chargePoint(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        this.point += amount;
    }

    public void deductPoint(Long amount) {
        if (this.point < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.point -= amount;
    }
}