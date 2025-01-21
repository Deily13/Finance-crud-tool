package com.example.Finance_crud_tool.controller;


import com.example.Finance_crud_tool.dto.ClientRequestDTO;

import com.example.Finance_crud_tool.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    public final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<String> createClient(@Valid @RequestBody ClientRequestDTO clientRequestDTO) {
        clientService.createClient(clientRequestDTO);
        return ResponseEntity.ok("Client created successfully");
    }
}
