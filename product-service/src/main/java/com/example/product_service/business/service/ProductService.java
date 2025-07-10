package com.example.product_service.business.service;



import com.example.product_service.model.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {

    List<Product> getAllProducts();

    List<Product> getProductsSortedByName();

    List<Product> getProductsSortedByPrice();

    List<Product> getProductsSortedByDescription();

    List<Product> getProductsSortedByCategory();

    Product createProduct(Product product) throws Exception;

    Optional<Product> findProductById(Long id) throws Exception;

    void deleteProduct(Long id);

    void updateProductQuantity(Product product);

    Product updateProduct(Product product) throws Exception;

    Optional<Product> findProductByName(String name);

    List<Product> findProductsByKeyword(String keyword);

    void sellProductById(Long id);
}
