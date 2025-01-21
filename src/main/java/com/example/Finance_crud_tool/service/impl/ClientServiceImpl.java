package com.example.Finance_crud_tool.service.impl;


import com.example.Finance_crud_tool.dto.ClientRequestDTO;
import com.example.Finance_crud_tool.entity.Client;
import com.example.Finance_crud_tool.repository.ClientRepository;
import com.example.Finance_crud_tool.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void createClient(ClientRequestDTO clientRequestDTO) {
        Client client = new Client();
        client.setIdentification_type(clientRequestDTO.identification_type());
        client.setIdentification_number(clientRequestDTO.identification_number());
        client.setName(clientRequestDTO.name());
        client.setLast_name(clientRequestDTO.last_name());
        client.setEmail(clientRequestDTO.email());
        client.setBirth_date(clientRequestDTO.birth_date());

        clientRepository.save(client);
    }


}
