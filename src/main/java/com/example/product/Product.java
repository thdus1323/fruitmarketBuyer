package com.example.product;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "product_tb")
@Data
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer productId;

    @Column(unique = true,length = 20, nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer productPrice;

    @Column(nullable = false)
    private Integer productQty;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
