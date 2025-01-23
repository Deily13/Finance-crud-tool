package com.example.Finance_crud_tool.dto;

import java.math.BigDecimal;

public record TransferenciaRequest(
        String originAccountNumber,
        String destinationAccountNumber,
        BigDecimal amount
) {}
