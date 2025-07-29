package com.example.products;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class SalesService {

    private final WebClient salesClient;

    @Value("${sales.service.url}")
    private String salesServiceUrl;

    public SalesService(@Qualifier("salesClient") WebClient salesClient) {
        this.salesClient = salesClient;
    }

    public void sellProduct(Long productId) {
        try {
            salesClient.post()
                .uri(salesServiceUrl + "/sell-product/{id}", productId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException(parseErrorMessage(e));
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while selling product: " + e.getMessage());
        }
    }

    private String parseErrorMessage(WebClientResponseException e) {
        try {
            String body = e.getResponseBodyAsString();
            int start = body.indexOf(":\"") + 2;
            int end = body.indexOf("\"", start);
            return body.substring(start, end);
        } catch (Exception parseEx) {
            return "Unknown error occurred";
        }
    }
}


