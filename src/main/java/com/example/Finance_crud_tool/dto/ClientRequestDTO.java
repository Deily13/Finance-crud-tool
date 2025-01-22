package com.example.Finance_crud_tool.dto;

import com.example.Finance_crud_tool.entity.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClientRequestDTO(

        @NotNull
        Client.Identification_type identification_type,

        Long identification_number,

        @NotNull
        @Size(min = 2)
        String name,

        String last_name,

        @Email
        String email,

        LocalDate birth_date
){
        }
