package com.example.products;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Value("${product.service.url}")
    private String productServiceUrl;
    @Value("${sales.service.url}")
    private String salesServiceUrl;


    @Bean
    @Qualifier("productClient")
    public WebClient productClient() {
        return WebClient.builder()
            .baseUrl(productServiceUrl)
            .build();
    }

    @Bean
    @Qualifier("salesClient")
    public WebClient salesClient() {
        return WebClient.builder()
            .baseUrl(salesServiceUrl)
            .build();
    }
}


