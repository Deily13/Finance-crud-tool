package com.example.Finance_crud_tool.controller;

import com.example.Finance_crud_tool.dto.CreateProductDto;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductDto createProductDto) {
        try {
            Product product = productService.createProduct(createProductDto);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }}
