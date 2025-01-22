package com.example.Finance_crud_tool.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "client")
@Entity(name = "Client")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(name = "identification_type", nullable = false)
    private Identification_type identification_type;

    public enum Identification_type{
        CC,
        CE,
        PASAPORTE,

    }

    @NotNull
    private Long identification_number;

    @NotNull
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String name;

    @NotNull
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    private String last_name;

    @NotNull
    @Email
    private String email;


    @NotNull
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate birth_date;


    private LocalDateTime creation_date = LocalDateTime.now();
    private LocalDateTime update_date = LocalDateTime.now();


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();


    @PrePersist
    public void prePersist() {
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
