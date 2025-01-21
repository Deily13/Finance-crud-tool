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

@Table(name = "client")
@Entity(name = "Client")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    private Long id;
    private String identification_type;
    private Long identification_number;

    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String name;

    @NotNull(message = "El apellido es obligatorio")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    private String last_name;

    @Email
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate birth_date;
    private LocalDateTime creation_date = LocalDateTime.now();
    private LocalDateTime update_date = LocalDateTime.now();

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
