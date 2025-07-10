package com.example.products;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("productClient")
    public WebClient productWebClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:8081")
            .build();
    }

    @Bean
    @Qualifier("salesClient")
    public WebClient salesWebClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build();
    }
}

