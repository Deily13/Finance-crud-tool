package com.example.Finance_crud_tool.dto;

import java.math.BigDecimal;

public record RetiroRequest(

        String originAccountNumber,
        BigDecimal amount
) {
}
