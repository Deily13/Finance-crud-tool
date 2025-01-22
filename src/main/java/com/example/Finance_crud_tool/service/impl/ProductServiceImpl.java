package com.example.Finance_crud_tool.service.impl;


import com.example.Finance_crud_tool.dto.CreateProductDto;
import com.example.Finance_crud_tool.entity.Client;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.repository.ClientRepository;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductServiceImpl  implements ProductService {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;


    public ProductServiceImpl(ProductRepository productRepository, ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    public Product createProduct(CreateProductDto createProductDto) throws Exception {

        Client client = clientRepository.findById(createProductDto.clientId())
                .orElseThrow(() -> new Exception("El cliente no existe"));

        if (createProductDto.accountType() == Product.AccountType.SAVINGS
                && createProductDto.balance().compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("El saldo de la cuenta de ahorros no puede ser menor a cero");
        }

        String lastAccountNumber = productRepository.findMaxAccountNumberStartingWith(
                createProductDto.accountType() == Product.AccountType.SAVINGS ? "53" : "33");

        // Lógica para generar el siguiente número de cuenta
        String accountNumber = generateNextAccountNumber(lastAccountNumber, createProductDto.accountType());

        // Crear la instancia del producto
        Product product = new Product();
        product.setAccountType(createProductDto.accountType());
        product.setAccount_number(accountNumber);
        product.setStatus(createProductDto.status());
        product.setBalance(createProductDto.balance());
        product.setExempt_GMF(createProductDto.exemptGMF());
        product.setClient(client); // Asignar el cliente al producto

        return productRepository.save(product);
    }

    private String generateNextAccountNumber(String lastAccountNumber, Product.AccountType accountType) {
        String prefix = accountType == Product.AccountType.SAVINGS ? "53" : "33";

        // Si no hay cuentas previas, comenzar con el primer número
        if (lastAccountNumber == null) {
            return prefix + String.format("%08d", 1); // Inicia con 00000001
        }

        // Extraer el número (sin el prefijo) y convertirlo a long
        long lastNumber = Long.parseLong(lastAccountNumber.substring(2));

        // Generar el siguiente número
        long nextNumber = lastNumber + 1;

        // Formatear el siguiente número con el prefijo y 8 dígitos
        return prefix + String.format("%08d", nextNumber);
    }
}
