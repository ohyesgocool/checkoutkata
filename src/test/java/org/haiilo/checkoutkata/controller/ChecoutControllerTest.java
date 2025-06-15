package org.haiilo.checkoutkata.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;  // For JSON serialization

    @Test
    void calculateTotal_returnsOkWithTotal() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(List.of("apple", "banana", "apple"));

        // Mock service call to return 130
        Mockito.when(checkoutService.calculateTotal(request.getItems())).thenReturn(130);

        mockMvc.perform(post("/v1/checkout/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("130"));
    }

    @Test
    void calculateTotal_withEmptyItems_returnsOkWithZero() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(List.of());

        Mockito.when(checkoutService.calculateTotal(request.getItems())).thenReturn(0);

        mockMvc.perform(post("/v1/checkout/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateTotal_missingItemsField_returnsBadRequest() throws Exception {
        // Send empty JSON without 'items'
        String invalidJson = "{}";

        mockMvc.perform(post("/v1/checkout/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateTotal_serviceThrowsException_returnsInternalServerError() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(List.of("apple"));

        Mockito.when(checkoutService.calculateTotal(anyList()))
                .thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(post("/v1/checkout/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}
