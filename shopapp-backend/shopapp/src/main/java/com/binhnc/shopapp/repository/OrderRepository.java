package com.binhnc.shopapp.repository;

import com.binhnc.shopapp.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Tìm các đơn hàng của 1 user nào đó
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE (:keyword IS NULL OR :keyword = '' " +
            "OR o.fullName LIKE %:keyword% OR o.address LIKE %:keyword%" +
            "OR o.note LIKE %:keyword% OR o.email LIKE %:keyword%)")
    Page<Order> findByKeyword(String keyword, Pageable pageable);
}
