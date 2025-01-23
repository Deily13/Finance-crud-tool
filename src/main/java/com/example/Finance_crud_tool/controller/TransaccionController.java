package com.example.Finance_crud_tool.controller;

import com.example.Finance_crud_tool.dto.TransferenciaRequest;
import com.example.Finance_crud_tool.entity.Transaccion;
import com.example.Finance_crud_tool.service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
}
