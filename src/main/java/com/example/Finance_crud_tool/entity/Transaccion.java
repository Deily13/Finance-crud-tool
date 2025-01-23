package com.example.Finance_crud_tool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "transaccion")
@Entity(name = "Transaccion")
@Setter
@Getter


public class Transaccion {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaccion_type", nullable = false)
    private Transaccion.Transaccion_type transaccion_type;

    public enum Transaccion_type {
        Consignment,
        Withdrawal,
        Transfer
        }

     private BigDecimal amount;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_account_id", nullable = false)
    private Product originAccount;

    // Cuenta de destino
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id", nullable = true)
    private Product destinationAccount;

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

}
