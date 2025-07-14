package com.example.sales_service;

import com.example.sales_service.web.SalesController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@Disabled
@WebMvcTest(SalesController.class)
@ExtendWith(MockitoExtension.class)

public class SalesControllerTests_DISABLED {

/*
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

        mockMvc.perform(post("/api/sales/sell-product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void testSellProductConflict() throws Exception {
        String errorMessage = "Out of stock";
        doThrow(new IllegalStateException(errorMessage)).when(service).sellProductById(productId);

        mockMvc.perform(post("/api/sales/sell-product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value(errorMessage));
    }

    @Test
    void testSellProductInternalError() throws Exception {
        doThrow(new RuntimeException("DB down")).when(service).sellProductById(productId);

        mockMvc.perform(post("/api/sales/sell-product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value("Unexpected error"));
    } */
}


