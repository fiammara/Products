package com.example.products;

import com.example.products.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {
    private final WebClient productClient;
    private final String productServiceUrl;

    public ProductService(WebClient productClient,
                          @Value("${product.service.url}") String productServiceUrl) {
        this.productClient = productClient;
        this.productServiceUrl = productServiceUrl;
    }

    public List<Product> getAllProducts() {
        try {
            return productClient.get()
                .uri(productServiceUrl)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();
        } catch (Exception e) {
            // Log and return empty
            return Collections.emptyList();
        }
    }

    public List<Product> getSortedProducts(String path) {
        try {
            return productClient.get()
                .uri(productServiceUrl + path)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Service error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error", e);
        }
    }

    public void deleteProduct(Long id) {
        productClient.delete()
            .uri(productServiceUrl + "/{id}", id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public Product findProductById(Long id, String jwt) {
        return productClient.get()
            .uri(productServiceUrl + "/find/{id}", id)
            .header("Authorization", "Bearer " + jwt)
            .retrieve()
            .bodyToMono(Product.class)
            .block();
    }

    public void updateProduct(Long id, Product product, String jwt) {
        productClient.put()
            .uri(productServiceUrl + "/products/{id}", id)
            .header("Authorization", "Bearer " + jwt)
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void addProduct(Product product) {
        productClient.post()
            .uri(productServiceUrl + "/add-product")
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Product.class)
            .block();
    }

    public List<Product> search(String keyword) {
        return productClient.get()
            .uri(productServiceUrl + "/search?keyword={keyword}", keyword)
            .retrieve()
            .bodyToFlux(Product.class)
            .collectList()
            .block();
    }
    public List<Product> getProductsSorted(String sortBy) {

        String url = "";
        if (sortBy != null && !sortBy.isBlank()) {
            url += "?sortBy=" + URLEncoder.encode(sortBy, StandardCharsets.UTF_8);
        }

        return productClient.get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Product.class)
            .collectList()
            .block();
    }

}
