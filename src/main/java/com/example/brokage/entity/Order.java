package com.example.brokage.entity;

import com.example.brokage.data.enums.Side;
import com.example.brokage.data.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "asset_name")
    private String assetName;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_side")
    private Side orderSide;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "price")
    private long price;

    @Column(name = "size")
    private int size;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
