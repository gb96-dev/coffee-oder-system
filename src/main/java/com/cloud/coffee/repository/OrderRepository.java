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

    /**
     * 최근 3일간 가장 많이 팔린 상위 3개 메뉴를 조회하는 쿼리
     * (DTO를 따로 만들지 않고 Object[] 배열 형태로 깔끔하게 꺼내옵니다!)
     */
    @Query("SELECT oi.menuId, oi.menuName, SUM(oi.quantity) as totalQuantity " +
            "FROM OrderItem oi " +
            "JOIN oi.orders o " +
            "WHERE o.createdAt >= :startDate " +
            "GROUP BY oi.menuId, oi.menuName " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findTopMenus(@Param("startDate") LocalDateTime startDate, Pageable pageable);
}