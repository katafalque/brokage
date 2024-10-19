package com.example.brokage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "ASSET")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "size")
    private long size;

    @Column(name = "usable_size")
    private long usableSize;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
