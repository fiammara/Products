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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapStructMapper productMapper;
    private final Validator validator;


    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapStructMapper productMapper, Validator validator) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.validator = validator;
    }


    public List<Product> getAllProducts() {

        List<ProductDAO> productDAOList = productRepository.findAll();
        log.info("Got product list. Size is: {}", productDAOList::size);
        return productDAOList.stream()
            .map(productMapper::productDAOToProduct)
            .collect(Collectors.toList());
    }

    public List<Product> getProductsSortedByName() {
        List<ProductDAO> productDAOList = productRepository.findAll();
        return productDAOList.stream()
            .map(productMapper::productDAOToProduct)
            .sorted(Comparator.comparing(Product::getName))
            .collect(Collectors.toList());

    }

    public List<Product> getProductsSortedByPrice() {
        List<ProductDAO> productDAOList = productRepository.findAll();
        return productDAOList.stream()
            .map(productMapper::productDAOToProduct)
            .sorted(Comparator.comparing(Product::getPrice))
            .collect(Collectors.toList());

    }

    public List<Product> getProductsSortedByDescription() {


        List<ProductDAO> listByDescription = productRepository.findAll();
        return listByDescription.stream()
            .map(productMapper::productDAOToProduct)
            .sorted(new ProductComparatorByDescription())
            .collect(Collectors.toList());
    }

    public List<Product> getProductsSortedByCategory() {
        List<ProductDAO> listByCategory = productRepository.findAll();

        return listByCategory.stream()
            .map(productMapper::productDAOToProduct)
            .sorted(new ProductComparatorByCategory())
            .collect(Collectors.toList());
    }

    public Product createProduct(@Valid Product product) {


        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {

            throw new ConstraintViolationException("Product info validation failed", violations);
        }

        ProductDAO productDAOSaved = productRepository.save(productMapper.productToDAO(product));
        product.setQuantity(product.getInitialQuantity());
        log.info("Repository saved product DAO: {}", productDAOSaved);
        return productMapper.productDAOToProduct(productDAOSaved);

    }


    public Optional<Product> findProductById(Long id) {

        Optional<Product> productOptional =
            productRepository.findById(id).flatMap(product -> Optional.ofNullable(productMapper.productDAOToProduct(product)));

        log.info("Product with id {} is {}", id, productOptional);
        return productOptional;

    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        log.info("Product with id {} is deleted", id);
    }
    @Transactional
    public void updateProductQuantity(Product product) {
        product.setQuantity(product.getQuantity() - 1);
        productRepository.save(productMapper.productToDAO(product));
        productRepository.flush();
    }


    @SneakyThrows

    public Product updateProduct(@Valid Product productToUpdate) {

        Set<ConstraintViolation<Product>> violations = validator.validate(productToUpdate);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation failed", violations);
        }
        if (!findProductById(productToUpdate.getId()).isPresent()) {
            throw new ChangeSetPersister.NotFoundException();
        }
        Product existing = findProductById(productToUpdate.getId()).get();
        existing.setName(productToUpdate.getName());
        existing.setDescription(productToUpdate.getDescription());
        existing.setPrice(productToUpdate.getPrice());
        existing.setQuantity(productToUpdate.getQuantity());
        existing.setCategory(productToUpdate.getCategory());
        ProductDAO productSaved =
            productRepository.save(productMapper.productToDAO(existing));
        log.info("Product updated: {}", () -> productSaved);
        return productMapper.productDAOToProduct(productSaved);

    }

    public Optional<Product> findProductByName(String name) {

        Optional<Product> productOptional = productRepository.findByName(name)
            .stream()
            .findFirst()
            .flatMap(productDAO -> Optional.ofNullable(productMapper.productDAOToProduct(productDAO)));

        log.info("Found product with name {} is {}", name, productOptional);
        return productOptional;

    }

    public List<Product> findProductsByKeyword(String keyword) {

        List<ProductDAO> productDAOList = productRepository.findByKeyword(keyword);

        log.info("Got product list by keyword. Size is: {}", productDAOList::size);
        return productDAOList.stream().map(productMapper::productDAOToProduct).collect(Collectors.toList());
    }

    public void sellProductById(Long id) {
        int retries = 3;
        while (retries > 0) {
            try {
                Product product = findProductById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

                if (product.getQuantity() <= 0) {
                    throw new InsufficientStockException(id);
                }

                product.setQuantity(product.getQuantity() - 1);
                updateProductQuantity(product);

                System.out.println("✅ Product sold: " + id);
                break; // success, exit loop

            } catch (OptimisticLockingFailureException e) {
                retries--;
                System.out.println("❌ Concurrency conflict detected for product ID: " + id + ", retries left: " + retries);
                if (retries == 0) throw e; // rethrow after max retries
                // Optionally add small delay here before retrying
            }
        }
    }
}