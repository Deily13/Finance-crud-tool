package com.example.Finance_crud_tool.controller;

import com.example.Finance_crud_tool.dto.ClientRequestDTO;
import com.example.Finance_crud_tool.entity.Client;
import com.example.Finance_crud_tool.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    public void createClient_ShouldReturnSuccess_WhenValidData() throws Exception {
        // Arrange
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
                Client.Identification_type.PASAPORTE,
                123456789L,
                "Morsa de verano",
                "Summer",
                "morsa.de.verano@gmail.com",
                LocalDate.of(1990, 6, 15)
        );

        // Convertir el DTO a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String clientRequestJson = objectMapper.writeValueAsString(clientRequestDTO);

        // Mock del comportamiento del servicio
        doNothing().when(clientService).createClient(any(ClientRequestDTO.class));

        // Act & Assert
        mockMvc.perform(post("/client")
                        .contentType("application/json")
                        .content(clientRequestJson))  // Usar el JSON convertido del DTO
                .andExpect(status().isOk())
                .andExpect(content().string("Client created successfully"));

        // Verificar que el servicio ha sido llamado una vez con el DTO adecuado
        verify(clientService, times(1)).createClient(any(ClientRequestDTO.class));
    }


    @Test
    void createClient_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        // Arrange
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
                Client.Identification_type.PASAPORTE,  // Valor válido para identification_type
                123456789L,                           // identification_number (opcional o puedes dejarlo null)
                "",                                   // name (invalid, empty string to trigger validation)
                "Doe",                                // last_name (puedes usar un valor válido)
                "john.doe@example.com",               // email (válido)
                LocalDate.of(1990, 6, 15)             // birth_date (válido)
        );

        // Act & Assert
        mockMvc.perform(post("/client")
                        .contentType("application/json")
                        .content("{\"identification_type\": \"PASSPORT\", \"identification_number\": 123456789, \"name\": \"\", \"last_name\": \"Doe\", \"email\": \"john.doe@example.com\", \"birth_date\": \"1990-06-15\"}"))
                .andExpect(status().isBadRequest());

        // Verificar que el servicio no se haya llamado debido a la validación fallida
        verify(clientService, never()).createClient(any(ClientRequestDTO.class));
    }


    @Test
    void updateClient_ShouldReturnSuccess_WhenValidData() throws Exception {
        // Arrange
        Long clientId = 1L;
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
                Client.Identification_type.PASAPORTE,  // Ejemplo de tipo de identificación (ajústalo según sea necesario)
                123456789L,                           // Número de identificación
                "John Doe Updated",                   // Nombre
                "Doe",                                // Apellido
                "john.doe.updated@example.com",       // Correo electrónico
                LocalDate.of(1990, 6, 15)             // Fecha de nacimiento
        );

        // Simular el comportamiento de updateClient
        doNothing().when(clientService).updateClient(eq(clientId), any(ClientRequestDTO.class));

        // Act & Assert
        mockMvc.perform(put("/client/{id}", clientId)
                        .contentType("application/json")
                        .content("{\"identification_type\": \"PASSPORT\", \"identification_number\": 123456789, \"name\": \"John Doe Updated\", \"last_name\": \"Doe\", \"email\": \"john.doe.updated@example.com\", \"birth_date\": \"1990-06-15\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente actualizado exitosamente."));

        // Verificar que el método updateClient del servicio fue llamado con los parámetros correctos
        verify(clientService, times(1)).updateClient(eq(clientId), any(ClientRequestDTO.class));
    }


    @Test
    void deleteClient_ShouldReturnSuccess_WhenValidId() throws Exception {
        // Arrange
        Long clientId = 1L;
        when(clientService.deleteClient(clientId)).thenReturn(new ResponseEntity<>("Cliente eliminado exitosamente.", HttpStatus.OK));

        // Act & Assert
        mockMvc.perform(delete("/client/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente eliminado exitosamente."));

        verify(clientService, times(1)).deleteClient(clientId);
    }

    @Test
    void deleteClient_ShouldReturnNotFound_WhenClientDoesNotExist() throws Exception {
        // Arrange
        Long clientId = 999L;
        when(clientService.deleteClient(clientId)).thenReturn(new ResponseEntity<>("Cliente no encontrado.", HttpStatus.NOT_FOUND));

        // Act & Assert
        mockMvc.perform(delete("/client/{id}", clientId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado."));

        verify(clientService, times(1)).deleteClient(clientId);
    }
}
