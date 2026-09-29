package com.shop1c.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "stock")
    private BigDecimal stock;

    @Column(name = "active", nullable = false)
    private boolean active = true;

}
