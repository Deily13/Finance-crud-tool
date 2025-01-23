package com.example.Finance_crud_tool.service.impl;

import com.example.Finance_crud_tool.dto.TransferenciaRequest;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.entity.Transaccion;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.repository.TransaccionRepository;
import com.example.Finance_crud_tool.service.TransaccionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final ProductRepository productRepository;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, ProductRepository productRepository) {
        this.transaccionRepository = transaccionRepository;
        this.productRepository = productRepository;
    }


    @Transactional
    public Transaccion processTransaction(TransferenciaRequest request) {

        Product originAccount = productRepository.findByAccountNumber(request.originAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta emisora no encontrada."));

        if (originAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente en la cuenta emisora.");
        }


        Product destinationAccount = productRepository.findByAccountNumber(request.destinationAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta receptora no encontrada."));

        originAccount.setBalance(originAccount.getBalance().subtract(request.amount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.amount()));

        productRepository.save(originAccount);
        productRepository.save(destinationAccount);

        Transaccion transaccion = new Transaccion();
        transaccion.setTransaccion_type(Transaccion.Transaccion_type.Transfer);
        transaccion.setAmount(request.amount());
        transaccion.setTimestamp(LocalDateTime.now());
        transaccion.setOriginAccount(originAccount);
        transaccion.setDestinationAccount(destinationAccount);

        return transaccionRepository.save(transaccion);
    }

}
