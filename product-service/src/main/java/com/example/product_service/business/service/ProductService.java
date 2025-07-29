package com.example.product_service.business.service;



import com.example.product_service.model.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {

    List<Product> getAllProducts();


    Product createProduct(Product product);

    Optional<Product> findProductById(Long id);

    void deleteProduct(Long id);

    void updateProductQuantity(Product product);

    Product updateProduct(Product product);

    Optional<Product> findProductByName(String name);

    List<Product> findProductsByKeyword(String keyword);

    void sellProductById(Long id);

    List<Product> getProductsSorted(String sortBy);
}
