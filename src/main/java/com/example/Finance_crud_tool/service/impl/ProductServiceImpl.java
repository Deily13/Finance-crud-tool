package com.example.Finance_crud_tool.service.impl;


import com.example.Finance_crud_tool.dto.CreateProductDto;
import com.example.Finance_crud_tool.entity.Client;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.repository.ClientRepository;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl  implements ProductService {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;


    public ProductServiceImpl(ProductRepository productRepository, ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Product createProduct(CreateProductDto createProductDto) throws Exception {

        Client client = clientRepository.findById(createProductDto.clientId())
                .orElseThrow(() -> new Exception("El cliente no existe"));

        if (createProductDto.accountType() == Product.AccountType.SAVINGS
                && createProductDto.balance().compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("El saldo de la cuenta de ahorros no puede ser menor a cero");
        }

        String lastAccountNumber = productRepository.findMaxAccountNumberStartingWith(
                createProductDto.accountType() == Product.AccountType.SAVINGS ? "53" : "33");

        System.out.println("Last Account Number: " + lastAccountNumber);

        String accountNumber = generateNextAccountNumber(lastAccountNumber, createProductDto.accountType());
        System.out.println("Generated Account Number: " + accountNumber);

        Product product = new Product();
        product.setAccountType(createProductDto.accountType());
        product.setAccountNumber(accountNumber);
        product.setStatus(createProductDto.status());
        product.setBalance(createProductDto.balance());
        product.setExempt_GMF(createProductDto.exemptGMF());
        product.setClient(client);

        if (createProductDto.status() == null) {
            product.setStatus(Product.Status.Activa);
        } else {
            product.setStatus(createProductDto.status());
        }

        System.out.println("Product to Save: " + product);

        return productRepository.save(product);
    }

    private String generateNextAccountNumber(String lastAccountNumber, Product.AccountType accountType) {
        String prefix = accountType == Product.AccountType.SAVINGS ? "53" : "33";

        if (lastAccountNumber == null) {
            // Generar el primer número si no hay cuentas previas
            return prefix + String.format("%08d", 1);
        }

        if (!lastAccountNumber.startsWith(prefix) || lastAccountNumber.length() != 10) {
            throw new IllegalArgumentException("Formato inválido para el número de cuenta: " + lastAccountNumber);
        }

        long lastNumber = Long.parseLong(lastAccountNumber.substring(2));

        long nextNumber = lastNumber + 1;

        return prefix + String.format("%08d", nextNumber);
    }


    @Transactional
    public void updateAccountStatus(String accountNumber, Product.Status newStatus) throws Exception {
        Product product = productRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new Exception("La cuenta no existe"));

        validateAccountStatusChange(product, newStatus);

        product.setStatus(newStatus);
        productRepository.save(product);
        productRepository.flush();
    }

    private void validateAccountStatusChange(Product product, Product.Status newStatus) {
        if (newStatus == Product.Status.Cancelada && product.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("La cuenta no puede ser bloqueada si el saldo es diferente de $0.");
        }
    }
}
