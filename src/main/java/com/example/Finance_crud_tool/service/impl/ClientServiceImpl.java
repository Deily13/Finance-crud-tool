package com.example.Finance_crud_tool.service.impl;


import com.example.Finance_crud_tool.dto.ClientRequestDTO;
import com.example.Finance_crud_tool.entity.Client;
import com.example.Finance_crud_tool.repository.ClientRepository;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public ClientServiceImpl(ClientRepository clientRepository, ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    private boolean isUnderage(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() < 18;
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

        if (isUnderage(client.getBirth_date())) {
            throw new IllegalArgumentException("El cliente es menor de edad y no puede ser registrado.");
        }
        clientRepository.save(client);
    }

    @Override
    public void updateClient(Long clientId, ClientRequestDTO clientRequestDTO) {

        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente con ID " + clientId + " no existe."));

        Optional.ofNullable(clientRequestDTO.identification_type())
                .ifPresent(existingClient::setIdentification_type);

        Optional.ofNullable(clientRequestDTO.identification_number())
                .ifPresent(existingClient::setIdentification_number);

        Optional.ofNullable(clientRequestDTO.name())
                .ifPresent(existingClient::setName);

        Optional.ofNullable(clientRequestDTO.last_name())
                .ifPresent(existingClient::setLast_name);

        Optional.ofNullable(clientRequestDTO.email())
                .ifPresent(existingClient::setEmail);

        Optional.ofNullable(clientRequestDTO.birth_date())
                .ifPresent(birthDate -> {
                    if (isUnderage(birthDate)) {
                        throw new IllegalArgumentException("El cliente es menor de edad y no puede ser actualizado.");
                    }
                    existingClient.setBirth_date(birthDate);
                });

        clientRepository.save(existingClient);
    }

    @Override
    public ResponseEntity<String> deleteClient(Long clientId) {

        if (!clientRepository.existsById(clientId)) {
            throw new IllegalArgumentException("El cliente con ID " + clientId + " no existe.");
        }

        boolean hasProducts = productRepository.existsByClientId(clientId);
        if (hasProducts) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El cliente con ID " + clientId + " no puede ser eliminado porque tiene productos asociados.");
        }

        clientRepository.deleteById(clientId);
        return ResponseEntity.ok("El cliente con ID " + clientId + " ha sido eliminado exitosamente.");
    }


}
