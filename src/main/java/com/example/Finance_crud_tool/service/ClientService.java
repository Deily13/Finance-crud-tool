package com.example.Finance_crud_tool.service;

import com.example.Finance_crud_tool.dto.ClientRequestDTO;
import jakarta.validation.Valid;

public interface ClientService {


    void createClient(@Valid ClientRequestDTO clientRequestDTO);
}



