package com.example.brokage.repository;

import com.example.brokage.entity.Order;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    @Query("SELECT a FROM Order a WHERE a.user.email = :email AND a.id = :orderId")
    Optional<Order> findByUserEmailAndOrderId(@Param("email") String email, @Param("orderId") String orderId);

    Page<Order> findAll(@Nullable Specification<Order> spec, @Nonnull Pageable pageable);
}
