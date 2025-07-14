package com.example.sales_service.business.service.impl;


import com.example.sales_service.business.handlers.InsufficientStockException;
import com.example.sales_service.business.handlers.ProductNotFoundException;
import com.example.sales_service.business.mappers.ProductMapStructMapper;
import com.example.sales_service.business.repository.ProductRepository;
import com.example.sales_service.business.service.SalesService;
import com.example.sales_service.model.Product;
import jakarta.validation.Validator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@Log4j2
public class SalesServiceImpl implements SalesService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ProductRepository salesRepository;
    private final ProductMapStructMapper productMapper;
    private final Validator validator;
    private final WebClient webClient;

    public SalesServiceImpl(ProductRepository salesRepository,
                            ProductMapStructMapper productMapper, Validator validator, WebClient webClient) {
        this.salesRepository = salesRepository;
        this.productMapper = productMapper;
        this.validator = validator;
        this.webClient = webClient;
    }


 /*   public void updateProductQuantity(Product product) {
        salesRepository.save(productMapper.productToDAO(product));
    } */


    public void sellProductById(Long id) {

        try {
            kafkaTemplate.send("product-events", id.toString());
            System.out.println("✅ Kafka message sent for productId: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
