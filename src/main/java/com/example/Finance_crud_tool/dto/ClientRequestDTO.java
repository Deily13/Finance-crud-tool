package com.example.Finance_crud_tool.dto;

import java.time.LocalDate;

public record ClientRequestDTO(
        Long id,

        String identification_type,

        Long identification_number,

        String name,

        String last_name,

        String email,

        LocalDate birth_date
){
        }
