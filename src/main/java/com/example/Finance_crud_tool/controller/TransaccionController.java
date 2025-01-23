package com.example.Finance_crud_tool.controller;

import com.example.Finance_crud_tool.dto.ConsignacionRequest;
import com.example.Finance_crud_tool.dto.RetiroRequest;
import com.example.Finance_crud_tool.dto.TransferenciaRequest;
import com.example.Finance_crud_tool.entity.Transaccion;
import com.example.Finance_crud_tool.service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/transaccion")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/transferir")
    public ResponseEntity<Transaccion> transferir(@RequestBody TransferenciaRequest request) {
        Transaccion transaccion = transaccionService.processTransaction(request);
        return ResponseEntity.ok(transaccion);
    }

    @PostMapping("/consignacion")
    public ResponseEntity<Transaccion> processConsignacion(@RequestBody ConsignacionRequest request) {
        try {
            Transaccion transaccion = transaccionService.processConsignacion(request);
            return ResponseEntity.ok(transaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/retiro")
    public ResponseEntity<Transaccion> processRetiro(@RequestBody RetiroRequest request) {
        try {
            Transaccion transaccion = transaccionService.processRetiro(request);
            return ResponseEntity.ok(transaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
