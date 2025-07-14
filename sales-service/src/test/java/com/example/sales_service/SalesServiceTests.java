package com.example.sales_service;


import com.example.sales_service.business.handlers.InsufficientStockException;
import com.example.sales_service.business.handlers.ProductNotFoundException;
import com.example.sales_service.business.mappers.ProductMapStructMapper;
import com.example.sales_service.business.repository.ProductRepository;
import com.example.sales_service.business.repository.model.ProductDAO;
import com.example.sales_service.business.service.impl.SalesServiceImpl;
import com.example.sales_service.model.Product;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Disabled
@ExtendWith(MockitoExtension.class)
class SalesServiceTests {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    @Spy
    private SalesServiceImpl salesService;
    @Mock
    private ProductMapStructMapper productMapper;


    private final Long existingProductId = 1L;
    private final Long missingProductId = 999L;




 /*   @Test
    void sellProductById_success() {
        Product product = new Product();
        product.setId(existingProductId);
        product.setQuantity(5);

        when(salesService.findProductById(existingProductId)).thenReturn(Optional.of(product));

        salesService.sellProductById(existingProductId);

        assertEquals(4, product.getQuantity());
        verify(salesService).updateProductQuantity(product);
    } */

    @Test
    void sellProductById_productNotFound() {
        when(productRepository.findById(missingProductId)).thenReturn(Optional.empty());

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> salesService.sellProductById(missingProductId));

        assertTrue(ex.getMessage().contains(missingProductId.toString()));
    }

    @Test
    void sellProductById_insufficientStock() {
        ProductDAO productDAO = new ProductDAO();
        productDAO.setId(existingProductId);

        Product product = new Product();
        product.setId(existingProductId);
        product.setQuantity(0);

        when(productRepository.findById(existingProductId)).thenReturn(Optional.of(productDAO));
        when(productMapper.productDAOToProduct(productDAO)).thenReturn(product);
        InsufficientStockException ex = assertThrows(InsufficientStockException.class, () -> salesService.sellProductById(existingProductId));

        assertEquals("Not enough quantity to sell", ex.getMessage());
    }

}






