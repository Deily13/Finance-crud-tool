package com.example.Finance_crud_tool.dto;

import com.example.Finance_crud_tool.entity.Product;

import java.math.BigDecimal;

public record CreateProductDto(
        Long clientId,
        Product.AccountType accountType,
        Product.Status status,
        BigDecimal balance,
        BigDecimal exemptGMF
) {
}
