package com.example.products;

import com.example.products.model.Category;
import com.example.products.model.Product;
import com.example.products.web.UIController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
//@Import({UIController.class, UIControllerTest.MockConfig.class})

public class UIControllerTest {
    /*

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static WebClient webClient;
    @SuppressWarnings("rawtypes")
    private static WebClient.RequestHeadersUriSpec uriSpec;
    @SuppressWarnings("rawtypes")
    private static WebClient.RequestHeadersSpec headersSpec;
    private static WebClient.ResponseSpec responseSpec;


    @Test
    void testDisplayProductList() throws Exception {

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("productList"))
            .andExpect(model().attributeExists("productList"))
            .andExpect(model().attribute("productList", Matchers.hasSize(2)))
            .andExpect(content().string(Matchers.containsString("Nike")))
            .andExpect(content().string(Matchers.containsString("NikeFake")));
    }


    @TestConfiguration
    static class MockConfig {

        @Bean
        @Qualifier("productClient")
        public WebClient.Builder webClientBuilder() {
            // Mock the WebClient and all the chained calls
            WebClient.Builder mockBuilder = mock(WebClient.Builder.class);
            WebClient mockWebClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            // Sample products to be returned by the mock
            Product input1 = new Product();
            input1.setId(1L);
            input1.setPrice(100.0);
            input1.setQuantity(5);
            input1.setName("Nike");
            input1.setCategory(Category.ACCESSORIES);

            Product input2 = new Product();
            input2.setId(2L);
            input2.setPrice(90.0);
            input2.setQuantity(2);
            input2.setName("NikeFake");
            input2.setCategory(Category.CLOTHES);

            List<Product> products = List.of(input1, input2);

            // Mock the fluent API calls on WebClient.Builder
            when(mockBuilder.baseUrl(anyString())).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenReturn(mockWebClient);

            // Mock the fluent API calls on WebClient
            when(mockWebClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToFlux(Product.class)).thenReturn(Flux.fromIterable(products));

            return mockBuilder;
        }
    } */

}