package com.example.products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "PRODUCT_SERVICE_URL=http://product-service:8081/api/products",
        "SALES_SERVICE_URL=http://sales-service:8082/api/sales"
    })
@AutoConfigureWebTestClient
class UIControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void displayProductList_integration() {
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .consumeWith(response -> {
                String body = response.getResponseBody();
                assertTrue(body != null && body.contains("product"));
            });
    }
}
