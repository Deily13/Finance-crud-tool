package com.example.Finance_crud_tool.controller;


import com.example.Finance_crud_tool.dto.ClientRequestDTO;

import com.example.Finance_crud_tool.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    public final ClientService clientService;

    public ClientController(ClientService clientService) {

        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<String> createClient(@Valid @RequestBody ClientRequestDTO clientRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors().toString());
        }
        clientService.createClient(clientRequestDTO);
        return ResponseEntity.ok("Client created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateClient(@PathVariable Long id, @RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        clientService.updateClient(id, clientRequestDTO);
        return ResponseEntity.ok("Cliente actualizado exitosamente.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Cliente con ID " + id + " eliminado exitosamente.");
    }

}
