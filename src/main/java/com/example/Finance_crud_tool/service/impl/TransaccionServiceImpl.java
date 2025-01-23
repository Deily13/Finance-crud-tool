package com.example.Finance_crud_tool.service.impl;

import com.example.Finance_crud_tool.dto.ConsignacionRequest;
import com.example.Finance_crud_tool.dto.RetiroRequest;
import com.example.Finance_crud_tool.dto.TransferenciaRequest;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.entity.Transaccion;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.repository.TransaccionRepository;
import com.example.Finance_crud_tool.service.TransaccionService;
import jakarta.transaction.Transactional;
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


    @Override
    @Transactional
    public Transaccion processTransaction(TransferenciaRequest request) {
        Product originAccount = getAccountByNumber(request.originAccountNumber(), "Cuenta emisora no encontrada.");
        Product destinationAccount = getAccountByNumber(request.destinationAccountNumber(), "Cuenta receptora no encontrada.");

        validateSufficientFunds(originAccount, request.amount());

        updateAccountBalances(originAccount, destinationAccount, request.amount());

        return saveTransaction(originAccount, destinationAccount, request.amount(), Transaccion.Transaccion_type.Transfer);
    }

    @Override
    @Transactional
    public Transaccion processConsignacion(ConsignacionRequest request) {
        Product destinationAccount = getAccountByNumber(request.destinationAccountNumber(), "Cuenta receptora no encontrada.");

        updateAccountBalances(null, destinationAccount, request.amount());

        return saveTransaction(null, destinationAccount, request.amount(), Transaccion.Transaccion_type.Consignment);
    }

    @Override
    @Transactional
    public Transaccion processRetiro(RetiroRequest request) {
        Product originAccount = getAccountByNumber(request.originAccountNumber(), "Cuenta emisora no encontrada.");

        validateSufficientFunds(originAccount, request.amount());

        updateAccountBalances(originAccount, null, request.amount());

        return saveTransaction(originAccount, null, request.amount(), Transaccion.Transaccion_type.Withdrawal);
    }

    // Métodos auxiliares privados para reducir la repetición de código

    private Product getAccountByNumber(String accountNumber, String errorMessage) {
        return productRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

    private void validateSufficientFunds(Product account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente en la cuenta.");
        }
    }

    private void updateAccountBalances(Product originAccount, Product destinationAccount, BigDecimal amount) {
        if (originAccount != null) {
            originAccount.setBalance(originAccount.getBalance().subtract(amount));
            productRepository.save(originAccount);
        }

        if (destinationAccount != null) {
            destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
            productRepository.save(destinationAccount);
        }
    }

    private Transaccion saveTransaction(Product originAccount, Product destinationAccount, BigDecimal amount, Transaccion.Transaccion_type type) {
        Transaccion transaccion = new Transaccion();
        transaccion.setTransaccion_type(type);
        transaccion.setAmount(amount);
        transaccion.setTimestamp(LocalDateTime.now());
        transaccion.setOriginAccount(originAccount);
        transaccion.setDestinationAccount(destinationAccount);

        return transaccionRepository.save(transaccion);
    }
}

