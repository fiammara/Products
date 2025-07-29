package com.example.products.web.webclient;


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

   // @Value("${user.service.url}")
   // private String userServiceUrl;

    @Value("${user.service.base-url}")
    private String baseUrl;

    @Value("${user.service.users-path}")
    private String usersPath;

    @Value("${user.service.auth-path}")
    private String authPath;


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

    @Bean
    @Qualifier("userWebClient")
    public WebClient userWebClient() {
        return WebClient.builder()
            .baseUrl(baseUrl + usersPath)
            .build();
    }

    @Bean
    @Qualifier("authWebClient")
    public WebClient authWebClient() {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}


