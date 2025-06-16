package org.haiilo.checkoutkata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.haiilo.checkoutkata.dto.CheckoutRequest;
import org.haiilo.checkoutkata.service.CheckoutService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void calculateTotal_returnsOkWithTotal() throws Exception {
        // Given: a checkout request with a list of items
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(List.of("apple", "banana", "apple"));

        // And: the service returns a total of 130
        Mockito.when(checkoutService.calculateTotal(request.getItems())).thenReturn(130);

        // When: the controller receives a POST request to calculate total
        // Then: it returns HTTP 200 OK with the correct total
        mockMvc.perform(post("/v1/checkout/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(130));
    }

    @Test
    void calculateTotal_withEmptyItems_returnsBadRequest() throws Exception {
        // Given: a checkout request with an empty list of items
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(List.of());

        // When: the controller receives a POST request
        // Then: it returns HTTP 400 Bad Request
        mockMvc.perform(post("/v1/checkout/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateTotal_missingItemsField_returnsBadRequest() throws Exception {
        // Given: a JSON payload missing the 'items' field
        String invalidJson = "{}";

        // When: the controller receives the malformed request
        // Then: it returns HTTP 400 Bad Request
        mockMvc.perform(post("/v1/checkout/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateTotal_serviceThrowsException_returnsInternalServerError() throws Exception {
        // Given: a valid checkout request
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(List.of("apple"));

        // And: the service throws a runtime exception
        Mockito.when(checkoutService.calculateTotal(anyList()))
                .thenThrow(new RuntimeException("Something went wrong"));

        // When: the controller receives the request
        // Then: it returns HTTP 500 Internal Server Error
        mockMvc.perform(post("/v1/checkout/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}
