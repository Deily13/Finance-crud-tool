package com.example.Finance_crud_tool.controller;

import com.example.Finance_crud_tool.entity.Transaccion;
import com.example.Finance_crud_tool.service.TransaccionService;
import com.example.Finance_crud_tool.service.impl.TransaccionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transaccion")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping
    public ResponseEntity<Transaccion> processTransaction(
            @RequestParam String originAccountNumber,
            @RequestParam String destinationAccountNumber,
            @RequestParam BigDecimal amount) {

        try {
            // Llamamos al servicio para procesar la transacción
            Transaccion transaccion = TransaccionService.processTransaction(originAccountNumber, destinationAccountNumber, amount);

            // Retornamos una respuesta exitosa con la transacción
            return ResponseEntity.ok(transaccion);
        } catch (IllegalArgumentException e) {
            // Si hay un error, retornamos una respuesta con el mensaje de error
            return ResponseEntity.badRequest().body(null);
        }
}
}
