package com.example.sales_service;

import com.example.sales_service.business.service.SalesService;
import com.example.sales_service.web.SalesController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(SalesController.class)
@ExtendWith(MockitoExtension.class)

public class SalesControllerTests {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SalesService service;

    private final Long productId = 1L;
    @Test
    void testSellProductSuccess() throws Exception {
        doNothing().when(service).sellProductById(productId);

        mockMvc.perform(post("/sales/sell-product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void testSellProductConflict() throws Exception {
        String errorMessage = "Out of stock";
        doThrow(new IllegalStateException(errorMessage)).when(service).sellProductById(productId);

        mockMvc.perform(post("/sales/sell-product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value(errorMessage));
    }

    @Test
    void testSellProductInternalError() throws Exception {
        doThrow(new RuntimeException("DB down")).when(service).sellProductById(productId);

        mockMvc.perform(post("/sales/sell-product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value("Unexpected error"));
    }
}


