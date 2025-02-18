package com.example.Finance_crud_tool.entity;


import jakarta.persistence.*;
import jakarta.transaction.Transaction;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "product")
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

    @Column(name = "account_number", unique = true, nullable = false, length = 10)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.Activa;

    public enum Status {
        Activa,
        Inactiva,
        ACTIVE, Cancelada
    }

    @NotNull
    private BigDecimal balance;

    private BigDecimal exempt_GMF;

    private LocalDateTime creation_date = LocalDateTime.now();

    private LocalDateTime update_date = LocalDateTime.now();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToMany(mappedBy = "originAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaccion> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaccion> receivedTransactions = new ArrayList<>();

    @PrePersist
    public void prePersist() {

        if (this.status == null) {
            this.status = Status.Activa;
        }

        if (this.creation_date == null) {
            this.creation_date = LocalDateTime.now();
        }
        if (this.update_date == null) {
            this.update_date = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.update_date = LocalDateTime.now();
    }
}
