package com.example.Finance_crud_tool.service.impl;


import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl  implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


}
