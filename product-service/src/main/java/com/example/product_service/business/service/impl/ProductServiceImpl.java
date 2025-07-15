package com.example.product_service.business.service.impl;


import com.example.product_service.business.comparators.ProductComparatorByCategory;
import com.example.product_service.business.comparators.ProductComparatorByDescription;
import com.example.product_service.business.handlers.InsufficientStockException;
import com.example.product_service.business.handlers.ProductNotFoundException;
import com.example.product_service.business.mappers.ProductMapStructMapper;
import com.example.product_service.business.repository.ProductRepository;
import com.example.product_service.business.repository.model.ProductDAO;
import com.example.product_service.business.service.ProductService;
import com.example.product_service.model.Product;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapStructMapper productMapper;


    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapStructMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;

    }

    @Override
    public List<Product> getAllProducts() {

        List<ProductDAO> productDAOList = productRepository.findAll();
        log.info("Got product list. Size is: {}", productDAOList.size());
        return productDAOList.stream()
            .map(productMapper::productDAOToProduct)
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsSortedByName() {

        List<ProductDAO> productDAOList = productRepository.findAll();
        return productDAOList.stream()
            .map(productMapper::productDAOToProduct)
            .sorted(Comparator.comparing(Product::getName))
            .collect(Collectors.toList());

    }

    @Override
    public List<Product> getProductsSortedByPrice() {

        List<ProductDAO> productDAOList = productRepository.findAll();
        return productDAOList.stream()
            .map(productMapper::productDAOToProduct)
            .sorted(Comparator.comparing(Product::getPrice))
            .collect(Collectors.toList());

    }

    @Override
    public List<Product> getProductsSortedByDescription() {

        List<ProductDAO> listByDescription = productRepository.findAll();
        return listByDescription.stream()
            .map(productMapper::productDAOToProduct)
            .sorted(new ProductComparatorByDescription())
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsSortedByCategory() {

        List<ProductDAO> listByCategory = productRepository.findAll();
        log.info("Fetched {} products from repository for sorting by category", listByCategory.size());

        List<Product> sortedProducts = listByCategory.stream()
            .map(productMapper::productDAOToProduct)
            .sorted(new ProductComparatorByCategory())
            .collect(Collectors.toList());
        log.info("Returning {} products sorted by category", sortedProducts.size());
        return sortedProducts;
    }

    @Override
    public Product createProduct(@Valid Product product) {

        ProductDAO productDAOSaved = productRepository.save(productMapper.productToDAO(product));
        product.setQuantity(product.getInitialQuantity());
        log.info("Repository saved product DAO: {}", productDAOSaved);
        return productMapper.productDAOToProduct(productDAOSaved);

    }

    @Override
    public Optional<Product> findProductById(Long id) {

        Optional<Product> productOptional =
            productRepository.findById(id).flatMap(product -> Optional.ofNullable(productMapper.productDAOToProduct(product)));

        log.info("Product with id {} is {}", id, productOptional);
        return productOptional;

    }

    @Override
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
        log.info("Product with id {} is deleted", id);
    }

    @Override
    @Transactional
    public void updateProductQuantity(Product product) {

        product.setQuantity(product.getQuantity() - 1);
        productRepository.saveAndFlush(productMapper.productToDAO(product));
        log.info("Updated product quantity for product id {}:  -> {}", product.getId(), product.getQuantity());
    }

    @Override
    public Product updateProduct(Product productToUpdate) {
        Product existing = findProductById(productToUpdate.getId())
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productToUpdate.getId()));

        existing.setName(productToUpdate.getName());
        existing.setDescription(productToUpdate.getDescription());
        existing.setPrice(productToUpdate.getPrice());
        existing.setQuantity(productToUpdate.getQuantity());
        existing.setCategory(productToUpdate.getCategory());

        ProductDAO saved = productRepository.save(productMapper.productToDAO(existing));
        log.info("Product updated: {}", saved);

        return productMapper.productDAOToProduct(saved);

    }

    @Override
    public Optional<Product> findProductByName(String name) {

        Optional<Product> productOptional = productRepository.findByName(name).stream()
            .findFirst()
            .map(productMapper::productDAOToProduct);

        log.info("Found product with name '{}': {}", name, productOptional.orElse(null));
        return productOptional;

    }

    @Override
    public List<Product> findProductsByKeyword(String keyword) {

        List<ProductDAO> productDAOList = productRepository.findByKeyword(keyword);
        log.info("Got product list by keyword '{}'. Size is: {}", keyword, productDAOList.size());
        return productDAOList.stream()
            .map(productMapper::productDAOToProduct)
            .collect(Collectors.toList());
    }

    @Override
    public void sellProductById(Long id) {

        int retries = 3;
        while (retries > 0) {
            try {
                Product product = findProductById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id.toString()));

                if (product.getQuantity() <= 0) {
                    throw new InsufficientStockException(id);
                }

                updateProductQuantity(product);
                log.info("Product sold: {}", id);
                break;

            } catch (OptimisticLockingFailureException e) {
                retries--;
                log.warn("Concurrency conflict detected for product ID: {}, retries left: {}", id, retries);
                if (retries == 0) throw e;

            }
        }
    }
}