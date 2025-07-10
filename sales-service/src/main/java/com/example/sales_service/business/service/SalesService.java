package com.example.sales_service.business.service;


import com.example.sales_service.model.Product;

import java.util.Optional;


public interface SalesService {


    Optional<Product> findProductById(Long id) throws Exception;

    void updateProductQuantity(Product product);



    void sellProductById(Long id);
}
