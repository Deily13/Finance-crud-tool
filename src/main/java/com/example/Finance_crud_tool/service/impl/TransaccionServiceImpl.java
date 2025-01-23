package com.example.Finance_crud_tool.service.impl;

import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.entity.Transaccion;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.repository.TransaccionRepository;
import com.example.Finance_crud_tool.service.TransaccionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final ProductRepository productRepository;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, ProductRepository productRepository) {
        this.transaccionRepository = transaccionRepository;
        this.productRepository = productRepository;
    }

    public Transaccion processTransaction(String originAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        Product originAccount = productRepository.findByAccountNumber(originAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta emisora no encontrada."));

        if (originAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente en la cuenta emisora.");
        }

        Product destinationAccount = productRepository.findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta receptora no encontrada."));

        // Realizar la transferencia
        originAccount.setBalance(originAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

        // Guardar los cambios en ambas cuentas
        productRepository.save(originAccount);
        productRepository.save(destinationAccount);

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTransaccion_type(Transaccion.Transaccion_type.Transfer);
        transaccion.setAmount(amount);
        transaccion.setOriginAccount(originAccount);
        transaccion.setDestinationAccount(destinationAccount);
        transaccion.setTimestamp(LocalDateTime.now());

        return transaccionRepository.save(transaccion);
    }

}
