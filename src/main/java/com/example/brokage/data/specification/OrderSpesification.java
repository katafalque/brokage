package com.example.brokage.data.specification;

import com.example.brokage.data.request.ListOrdersRequest;
import com.example.brokage.entity.Order;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.UUID;

public class OrderSpesification {
    public static final String ID = "id";
    public static final String DATE = "date";

    private OrderSpesification() {

    }

    public static Specification<Order> filterBy(String userId, Date startDate, Date endDate) {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate predicate = cb.conjunction();

            if (userId != null ) {
                predicate = cb.and(predicate, cb.equal(root.get("user").get("id"), UUID.fromString(userId)));
            }

            if (startDate != null ) {
                predicate = cb.and(predicate, cb.greaterThan(root.get("createdAt"), startDate));
            }

            if (endDate != null) {
                predicate = cb.and(predicate, cb.lessThan(root.get("createdAt"), endDate));
            }

            return predicate;
        };
    }
}
