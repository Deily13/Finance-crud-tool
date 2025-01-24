package com.example.Finance_crud_tool.service;

import com.example.Finance_crud_tool.dto.ClientRequestDTO;
import com.example.Finance_crud_tool.entity.Client;
import com.example.Finance_crud_tool.repository.ClientRepository;
import com.example.Finance_crud_tool.repository.ProductRepository;
import com.example.Finance_crud_tool.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    void createClient_ShouldSaveClient_WhenDataIsValid() {
        // Arrange
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
                Client.Identification_type.CC,
                123456789L,
                "Morsa",
                "verano",
                "morsaverano@gmail.com",
                LocalDate.of(2000, 1, 1)
        );


        clientService.createClient(clientRequestDTO);

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void createClient_ShouldThrowException_WhenClientIsUnderage() {
        // Arrange
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
                Client.Identification_type.CC,
                123456789L,
                "Morsa",
                "verano",
                "morsaverano@gmail.com",
                LocalDate.now().minusYears(17)
        );


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> clientService.createClient(clientRequestDTO));
        assertEquals("El cliente es menor de edad y no puede ser registrado.", exception.getMessage());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void updateClient_ShouldUpdateClient_WhenDataIsValid() {
        // Arrange
        Long clientId = 1L;
        Client existingClient = new Client();
        existingClient.setId(clientId);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
                Client.Identification_type.CC,
                987654321L,
                "Deily",
                "Martinez",
                "Deily@gmail.com",
                LocalDate.of(1995, 5, 15)
        );

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));

        clientService.updateClient(clientId, clientRequestDTO);

        verify(clientRepository, times(1)).save(existingClient);
        assertEquals("CC", existingClient.getIdentification_type().name());
        assertEquals(987654321L, existingClient.getIdentification_number());
        assertEquals("Deily", existingClient.getName());
        assertEquals("Martinez", existingClient.getLast_name());
        assertEquals("Deily@gmail.com", existingClient.getEmail());
        assertEquals(LocalDate.of(1995, 5, 15), existingClient.getBirth_date());
    }

    @Test
    void deleteClient_ShouldDeleteClient_WhenNoProductsExist() {

        Long clientId = 1L;
        when(clientRepository.existsById(clientId)).thenReturn(true);
        when(productRepository.existsByClientId(clientId)).thenReturn(false);

        var response = clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
        assertEquals("El cliente con ID " + clientId + " ha sido eliminado exitosamente.", response.getBody());
    }

    @Test
    void deleteClient_ShouldThrowException_WhenClientDoesNotExist() {

        Long clientId = 1L;
        when(clientRepository.existsById(clientId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> clientService.deleteClient(clientId));
        assertEquals("El cliente con ID " + clientId + " no existe.", exception.getMessage());
        verify(clientRepository, never()).deleteById(clientId);
    }

    @Test
    void deleteClient_ShouldReturnConflict_WhenClientHasProducts() {

        Long clientId = 1L;
        when(clientRepository.existsById(clientId)).thenReturn(true);
        when(productRepository.existsByClientId(clientId)).thenReturn(true);

        var response = clientService.deleteClient(clientId);

        verify(clientRepository, never()).deleteById(clientId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El cliente con ID " + clientId + " no puede ser eliminado porque tiene productos asociados.", response.getBody());
    }
}
