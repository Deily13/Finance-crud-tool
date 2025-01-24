package com.example.Finance_crud_tool.service;


import com.example.Finance_crud_tool.dto.CreateProductDto;
import com.example.Finance_crud_tool.entity.Client;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.repository.ClientRepository;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Client mockClient;
    private CreateProductDto mockCreateProductDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockClient = new Client();
        mockClient.setId(1L);
        mockClient.setIdentification_number(987654321L);

        mockCreateProductDto = new CreateProductDto(
                1L,
                Product.AccountType.SAVINGS,
                Product.Status.Activa,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(0)


        );
    }

    @Test
    void createProduct_ShouldReturnProduct_WhenValidData() throws Exception {

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(mockClient));

        when(productRepository.findMaxAccountNumberStartingWith(any(String.class))).thenReturn("5300000000");

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product product = productService.createProduct(mockCreateProductDto);

        verify(clientRepository, times(1)).findById(any(Long.class));
        verify(productRepository, times(1)).findMaxAccountNumberStartingWith(any(String.class));
        verify(productRepository, times(1)).save(any(Product.class));


        assert product.getAccountNumber().startsWith("53");
        assert product.getBalance().compareTo(BigDecimal.valueOf(1000)) == 0;
        assert product.getStatus() == Product.Status.Activa;
    }

    @Test
    void createProduct_ShouldThrowException_WhenClientNotFound() {

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());


        try {
            productService.createProduct(mockCreateProductDto);
        } catch (Exception e) {
            assert e.getMessage().equals("El cliente no existe");
        }

        verify(clientRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void updateAccountStatus_ShouldUpdateStatus_WhenValid() throws Exception {

        Product mockProduct = new Product();
        mockProduct.setAccountNumber("5300000001");
        mockProduct.setBalance(BigDecimal.ZERO);
        mockProduct.setStatus(Product.Status.Activa);

        when(productRepository.findByAccountNumber(any(String.class))).thenReturn(Optional.of(mockProduct));

        productService.updateAccountStatus("5300000001", Product.Status.Cancelada);

        verify(productRepository, times(1)).findByAccountNumber(any(String.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateAccountStatus_ShouldThrowException_WhenBalanceIsNotZero() {

        Product mockProduct = new Product();
        mockProduct.setAccountNumber("5300000001");
        mockProduct.setBalance(BigDecimal.valueOf(500));
        mockProduct.setStatus(Product.Status.Activa);

        when(productRepository.findByAccountNumber(any(String.class))).thenReturn(Optional.of(mockProduct));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateAccountStatus("5300000001", Product.Status.Cancelada);
        });
        assertEquals("La cuenta no puede ser bloqueada si el saldo es diferente de $0.", thrown.getMessage());

        verify(productRepository, times(1)).findByAccountNumber(any(String.class));
    }
}
