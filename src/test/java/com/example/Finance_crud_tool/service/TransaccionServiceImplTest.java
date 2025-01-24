package com.example.Finance_crud_tool.service;

import com.example.Finance_crud_tool.dto.ConsignacionRequest;
import com.example.Finance_crud_tool.dto.RetiroRequest;
import com.example.Finance_crud_tool.dto.TransferenciaRequest;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.entity.Transaccion;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.repository.TransaccionRepository;
import com.example.Finance_crud_tool.service.impl.TransaccionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransaccionServiceImplTest {

    private TransaccionServiceImpl transaccionService;
    private TransaccionRepository transaccionRepository;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        transaccionRepository = mock(TransaccionRepository.class);
        productRepository = mock(ProductRepository.class);
        transaccionService = new TransaccionServiceImpl(transaccionRepository, productRepository);
    }

    @Test
    void processTransaction_ShouldSaveTransaction_WhenValidData() {

        Product originAccount = new Product();
        originAccount.setAccountNumber("123456");
        originAccount.setBalance(BigDecimal.valueOf(1000));

        Product destinationAccount = new Product();
        destinationAccount.setAccountNumber("654321");
        destinationAccount.setBalance(BigDecimal.valueOf(500));

        when(productRepository.findByAccountNumber("123456")).thenReturn(Optional.of(originAccount));
        when(productRepository.findByAccountNumber("654321")).thenReturn(Optional.of(destinationAccount));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransferenciaRequest request = new TransferenciaRequest("123456", "654321", BigDecimal.valueOf(200));


        Transaccion result = transaccionService.processTransaction(request);


        assertNotNull(result);
        assertEquals(Transaccion.Transaccion_type.Transfer, result.getTransaccion_type());
        assertEquals(BigDecimal.valueOf(200), result.getAmount());
        assertEquals("123456", result.getOriginAccount().getAccountNumber());
        assertEquals("654321", result.getDestinationAccount().getAccountNumber());

        verify(productRepository, times(1)).save(originAccount);
        verify(productRepository, times(1)).save(destinationAccount);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));

        assertEquals(BigDecimal.valueOf(800), originAccount.getBalance());
        assertEquals(BigDecimal.valueOf(700), destinationAccount.getBalance());
    }

    @Test
    void processConsignacion_ShouldSaveTransaction_WhenValidData() {

        Product destinationAccount = new Product();
        destinationAccount.setAccountNumber("654321");
        destinationAccount.setBalance(BigDecimal.valueOf(500));

        when(productRepository.findByAccountNumber("654321")).thenReturn(Optional.of(destinationAccount));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsignacionRequest request = new ConsignacionRequest("654321", BigDecimal.valueOf(300));

        Transaccion result = transaccionService.processConsignacion(request);

        assertNotNull(result);
        assertEquals(Transaccion.Transaccion_type.Consignment, result.getTransaccion_type());
        assertEquals(BigDecimal.valueOf(300), result.getAmount());
        assertNull(result.getOriginAccount());
        assertEquals("654321", result.getDestinationAccount().getAccountNumber());

        verify(productRepository, times(1)).save(destinationAccount);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));

        assertEquals(BigDecimal.valueOf(800), destinationAccount.getBalance());
    }

    @Test
    void processRetiro_ShouldSaveTransaction_WhenValidData() {
        // Arrange
        Product originAccount = new Product();
        originAccount.setAccountNumber("123456");
        originAccount.setBalance(BigDecimal.valueOf(1000));

        when(productRepository.findByAccountNumber("123456")).thenReturn(Optional.of(originAccount));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RetiroRequest request = new RetiroRequest("123456", BigDecimal.valueOf(200));

        Transaccion result = transaccionService.processRetiro(request);

        assertNotNull(result);
        assertEquals(Transaccion.Transaccion_type.Withdrawal, result.getTransaccion_type());
        assertEquals(BigDecimal.valueOf(200), result.getAmount());
        assertEquals("123456", result.getOriginAccount().getAccountNumber());
        assertNull(result.getDestinationAccount());

        verify(productRepository, times(1)).save(originAccount);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));

        assertEquals(BigDecimal.valueOf(800), originAccount.getBalance());
    }

    @Test
    void processTransaction_ShouldThrowException_WhenInsufficientFunds() {

        Product originAccount = new Product();
        originAccount.setAccountNumber("123456");
        originAccount.setBalance(BigDecimal.valueOf(100));

        Product destinationAccount = new Product();
        destinationAccount.setAccountNumber("654321");
        destinationAccount.setBalance(BigDecimal.valueOf(500));

        when(productRepository.findByAccountNumber("123456")).thenReturn(Optional.of(originAccount));
        when(productRepository.findByAccountNumber("654321")).thenReturn(Optional.of(destinationAccount));

        TransferenciaRequest request = new TransferenciaRequest("123456", "654321", BigDecimal.valueOf(200));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> transaccionService.processTransaction(request));
        assertEquals("Saldo insuficiente en la cuenta.", exception.getMessage());

        verify(productRepository, never()).save(any(Product.class));
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }
}
