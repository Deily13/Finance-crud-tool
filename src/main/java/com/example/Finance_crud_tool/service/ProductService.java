package com.example.Finance_crud_tool.service;

import com.example.Finance_crud_tool.dto.CreateProductDto;
import com.example.Finance_crud_tool.entity.Product;


public interface ProductService {
    Product createProduct(CreateProductDto createProductDto) throws Exception;

    void updateAccountStatus(String accountNumber, Product.Status newStatus) throws Exception;
}
