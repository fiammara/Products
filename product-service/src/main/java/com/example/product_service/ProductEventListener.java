package com.example.product_service;

import com.example.product_service.business.repository.ProductRepository;
import com.example.product_service.business.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventListener {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @KafkaListener(topics = "product-events", groupId = "product-group")
    public void handleProductSold(String productId) {
        try {
            long id = Long.parseLong(productId);
            productService.sellProductById(id);
        } catch (NumberFormatException e) {

            System.err.println("Invalid productId received: " + productId);
        } catch (Exception e) {

            System.err.println("Error processing product sold event: " + e.getMessage());
        }
    }
}
