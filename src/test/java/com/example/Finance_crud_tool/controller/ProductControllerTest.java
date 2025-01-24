package com.example.Finance_crud_tool.controller;

import com.example.Finance_crud_tool.dto.CreateProductDto;
import com.example.Finance_crud_tool.entity.Product;
import com.example.Finance_crud_tool.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



@SpringBootTest
public class ProductControllerTest {

    private final ProductService productService;

    private final WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    public ProductControllerTest(ProductService productService, WebApplicationContext webApplicationContext) {
        this.productService = productService;
        this.webApplicationContext = webApplicationContext;
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateProduct() throws Exception {
        Long clientId = 1L;
        Product.AccountType accountType = Product.AccountType.SAVINGS;
        Product.Status status = Product.Status.ACTIVE;
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal exemptGMF = new BigDecimal("50.00");

        CreateProductDto createProductDto = new CreateProductDto(clientId, accountType, status, balance, exemptGMF);

        // Simulamos la creación del producto
        Product product = productService.createProduct(createProductDto);  // Asegúrate de que el servicio cree el producto correctamente

        // Convertir el DTO a JSON utilizando ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(createProductDto);

        // Ejecutar la solicitud POST y verificar la respuesta
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())  // Verificar que la respuesta sea OK (200)
                .andExpect(content().json("{\"id\": " + product.getId() + ", \"clientId\": " + clientId + ", \"accountType\": \"SAVINGS\", \"status\": \"ACTIVE\", \"balance\": \"1000.00\", \"exemptGMF\": \"50.00\"}"));
    }



    @Test
    public void testUpdateAccountStatus() throws Exception {
        // Define el request JSON para actualizar el estado
        String requestJson = "{\"accountNumber\": \"12345\", \"newStatus\": \"ACTIVE\"}";

        // Realiza la llamada al endpoint
        mockMvc.perform(post("/products/update-status")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("El estado de la cuenta ha sido actualizado exitosamente."));
    }

    @Test
    public void testUpdateAccountStatusBadRequest() throws Exception {
        // Define el request JSON con un valor erróneo
        String requestJson = "{\"accountNumber\": \"invalid\", \"newStatus\": \"INVALID_STATUS\"}";

        // Realiza la llamada al endpoint
        mockMvc.perform(post("/products/update-status")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid account number"));
    }

    @Test
    public void testUpdateAccountStatusInternalError() throws Exception {
        // Define el request JSON para simular un error interno
        String requestJson = "{\"accountNumber\": \"12345\", \"newStatus\": \"ACTIVE\"}";

        // Simula un error interno en el servicio para este test (esto es solo un ejemplo básico)
        // En un escenario real, puedes agregar lógica adicional para forzar excepciones dentro del servicio

        mockMvc.perform(post("/products/update-status")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error interno al procesar la solicitud."));
    }

}
