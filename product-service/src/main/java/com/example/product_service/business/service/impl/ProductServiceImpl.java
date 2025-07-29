package com.example.product_service.business.service.impl;


import com.example.product_service.business.handlers.InsufficientStockException;
import com.example.product_service.business.handlers.ProductNotFoundException;
import com.example.product_service.business.mappers.ProductMapStructMapper;
import com.example.product_service.business.repository.ProductRepository;
import com.example.product_service.business.repository.model.ProductDAO;
import com.example.product_service.business.service.ProductService;
import com.example.product_service.model.Product;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;


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
            .toList();
    }


    @Override
    public List<Product> getProductsSorted(String sortBy) {
        if (!StringUtils.hasText(sortBy)) {
            sortBy = "name";
        }
        Sort sort = Sort.by(sortBy);

        List<ProductDAO> productDAOs = productRepository.findAll(sort);

        return productDAOs.stream()
            .map(productMapper::productDAOToProduct)
            .toList();
    }


    @Override
    public Product createProduct(Product product) {

        product.setQuantity(product.getInitialQuantity());

        ProductDAO savedDAO = productRepository.save(productMapper.productToDAO(product));
        log.info("Created product with ID: {}", savedDAO.getId());

        return productMapper.productDAOToProduct(savedDAO);

    }

    @Override
    public Optional<Product> findProductById(Long id) {
        Optional<Product> productOptional =
            productRepository.findById(id)
                .map(productMapper::productDAOToProduct);

        log.info("Product with id {} is {}", id, productOptional);
        return productOptional;
    }

    @Override
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
        log.info("Product with id {} is deleted", id);
    }

    @Transactional
    @Override
    public void updateProductQuantity(Product product) {
        ProductDAO dao = productRepository.findById(product.getId())
            .orElseThrow(() -> new ProductNotFoundException("Product not found: " + product.getId()));

        if (dao.getQuantity() <= 0) {
            throw new InsufficientStockException(product.getId());
        }

        dao.setQuantity(dao.getQuantity() - 1);
        productRepository.saveAndFlush(dao);

        log.info("Updated product quantity for product id {}:  -> {}", dao.getId(), dao.getQuantity());
    }

    @Override
    @Transactional
    public Product updateProduct(Product productToUpdate) {
        Product existing = findProductById(productToUpdate.getId())
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productToUpdate.getId()));

        existing.setName(productToUpdate.getName());
        existing.setDescription(productToUpdate.getDescription());
        existing.setPrice(productToUpdate.getPrice());
        existing.setQuantity(productToUpdate.getQuantity());
        existing.setCategory(productToUpdate.getCategory());

        ProductDAO saved = productRepository.saveAndFlush(productMapper.productToDAO(existing));
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
            .toList();
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