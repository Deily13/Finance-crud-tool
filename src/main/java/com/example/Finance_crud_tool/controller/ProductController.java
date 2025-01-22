package com.example.Finance_crud_tool.controller;

import com.example.Finance_crud_tool.dto.CreateProductDto;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    }

    @PostMapping("/update-status")
    public ResponseEntity<String> updateAccountStatus(@RequestBody UpdateAccountStatusRequest request) {
        try {
            productService.updateAccountStatus(request.accountNumber(), request.newStatus());
            return ResponseEntity.ok("El estado de la cuenta ha sido actualizado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al procesar la solicitud.");
        }
    }

    public record UpdateAccountStatusRequest(String accountNumber, Product.Status newStatus) {}
}
