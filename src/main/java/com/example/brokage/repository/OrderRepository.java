package com.example.brokage.repository;

import com.example.brokage.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT a FROM Order a WHERE a.user.email = :email AND a.id = :orderId")
    Optional<Order> findByUserEmailAndOrderId(@Param("email") String email, @Param("orderId") String orderId);
}
