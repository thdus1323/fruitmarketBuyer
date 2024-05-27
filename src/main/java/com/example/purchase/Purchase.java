package com.example.purchase;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table(name = "purchase_tb")
@Entity
public class Purchase {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer purId;

    @Column
    private Integer buyerId;

    @Column
    private String productId;

    @Column
    private String productName;

    @Column
    private Integer productPrice;

    @Column(nullable = false)
    private Integer productQty;

    @Column(nullable = false)
    private Integer purQty;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
