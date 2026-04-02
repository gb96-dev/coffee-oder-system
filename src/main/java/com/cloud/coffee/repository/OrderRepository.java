package com.cloud.coffee.repository;

import com.cloud.coffee.domain.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT oi.menuId, oi.menuName, SUM(oi.quantity) as totalQuantity " +
            "FROM OrderItem oi " +
            "JOIN oi.orders o " +
            "WHERE o.createdAt >= :startDate " +
            "GROUP BY oi.menuId, oi.menuName " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findTopMenus(@Param("startDate") LocalDateTime startDate, Pageable pageable);
}