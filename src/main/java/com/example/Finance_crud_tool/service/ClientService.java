package com.example.Finance_crud_tool.service;

import com.example.Finance_crud_tool.dto.ClientRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface ClientService {


    void createClient(@Valid ClientRequestDTO clientRequestDTO);

    void updateClient(Long clientId, ClientRequestDTO clientRequestDTO);

    ResponseEntity<String> deleteClient(Long id);

}



