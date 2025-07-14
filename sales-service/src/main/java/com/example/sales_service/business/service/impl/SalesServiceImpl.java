package com.example.sales_service.business.service.impl;


import com.example.sales_service.business.handlers.InsufficientStockException;
import com.example.sales_service.business.handlers.ProductNotFoundException;
import com.example.sales_service.business.mappers.ProductMapStructMapper;
import com.example.sales_service.business.repository.ProductRepository;
import com.example.sales_service.business.service.SalesService;
import com.example.sales_service.model.Product;
import jakarta.validation.Validator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@Log4j2
public class SalesServiceImpl implements SalesService {


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


    public void updateProductQuantity(Product product) {
        salesRepository.save(productMapper.productToDAO(product));
    }


    public void sellProductById(Long id) {

        Product product = findProductById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        if (product.getQuantity() <= 0) {
            throw new InsufficientStockException(id);
        }

        product.setQuantity(product.getQuantity() - 1);
        updateProductQuantity(product);
    }

    public Optional<Product> findProductById(Long id) {

        Optional<Product> productOptional =
            salesRepository.findById(id).flatMap(product -> Optional.ofNullable(productMapper.productDAOToProduct(product)));

        log.info("Product with id {} is {}", id, productOptional);
        return productOptional;

    }

}
