package com.example.Finance_crud_tool.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "produtc")
@Entity(name = "Product")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    public enum AccountType {
        SAVINGS,
        CURRENT
    }

    private int account_number;

    private String status;

    private BigDecimal balance;

    private BigDecimal exempt_GMF;

    private LocalDateTime creation_date = LocalDateTime.now();

    private LocalDateTime update_date = LocalDateTime.now();

}
