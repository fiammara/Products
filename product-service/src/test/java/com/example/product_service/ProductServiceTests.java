package com.example.product_service;


import com.example.product_service.business.handlers.InsufficientStockException;
import com.example.product_service.business.handlers.ProductNotFoundException;
import com.example.product_service.business.mappers.ProductMapStructMapper;
import com.example.product_service.business.repository.ProductRepository;
import com.example.product_service.business.repository.model.ProductDAO;
import com.example.product_service.business.service.impl.ProductServiceImpl;
import com.example.product_service.model.Category;
import com.example.product_service.model.Product;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    @Mock
    private ProductRepository repository;
    @Spy
    @InjectMocks
    private ProductServiceImpl service;
    @Mock
    private ProductMapStructMapper mapper;
    @Mock
    private Validator validator;
    private final Long existingProductId = 1L;
    private final Long missingProductId = 999L;
    private List<Product> products;
    private List<ProductDAO> productDAOS;
    private Product product;
    private ProductDAO productDAO;

    @BeforeEach
    public void init() {
        product = createProduct();
        productDAO = createProductDAO();
        products = createProductList();
        productDAOS = createProductDAOList();
    }

    private ProductDAO createProductDAO() {
        ProductDAO dao = new ProductDAO();
        dao.setId(1L);
        dao.setName("Sample Product");
        dao.setDescription("new modern");
        dao.setQuantity(10);
        dao.setPrice(100.0);
        dao.setCategory(Category.ACCESSORIES);
        return dao;

    }

    private Product createProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setDescription("New modern");
        product.setQuantity(10);
        product.setInitialQuantity(10);
        product.setPrice(100.0);
        product.setCategory(Category.ACCESSORIES);
        return product;

    }

    @Test
    void findAllProducts() {
        when(repository.findAll()).thenReturn(productDAOS);
        when(mapper.productDAOToProduct(any())).thenReturn(product);

        List<Product> productList = service.getAllProducts();

        assertEquals(2, productList.size());
        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).productDAOToProduct(any());
    }

    @Test
    void findProductById() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.of(productDAO));
        when(mapper.productDAOToProduct(productDAO)).thenReturn(product);

        Optional<Product> productReturned = service.findProductById(product.getId());
        assertTrue(productReturned.isPresent(), "Expected product to be present");
        assertEquals(product.getId(), productReturned.get().getId());
        assertEquals(product.getName(), productReturned.get().getName());
        assertEquals(product.getPrice(), productReturned.get().getPrice());
        assertEquals(product.getCategory(), productReturned.get().getCategory());
        assertEquals(product.getDescription(), productReturned.get().getDescription());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getProductsSortedByName_shouldReturnSortedListByName() {

        ProductDAO productDAO1 = new ProductDAO();
        productDAO1.setName("Banana");

        ProductDAO productDAO2 = new ProductDAO();
        productDAO2.setName("Apple");

        List<ProductDAO> daoList = List.of(productDAO1, productDAO2);

        when(repository.findAll()).thenReturn(daoList);

        Product product1 = new Product();
        product1.setName("Banana");

        Product product2 = new Product();
        product2.setName("Apple");

        when(mapper.productDAOToProduct(productDAO1)).thenReturn(product1);
        when(mapper.productDAOToProduct(productDAO2)).thenReturn(product2);

        List<Product> result = service.getProductsSortedByName();

        assertEquals(2, result.size());
        assertEquals("Apple", result.get(0).getName());
        assertEquals("Banana", result.get(1).getName());

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).productDAOToProduct(productDAO1);
        verify(mapper, times(1)).productDAOToProduct(productDAO2);
    }

    @Test
    void getProductsSortedByPrice_shouldReturnListSortedByPrice() {

        ProductDAO productDAO1 = new ProductDAO();
        productDAO1.setPrice(200);

        ProductDAO productDAO2 = new ProductDAO();
        productDAO2.setPrice(100);

        List<ProductDAO> daoList = List.of(productDAO1, productDAO2);
        when(repository.findAll()).thenReturn(daoList);

        Product product1 = new Product();
        product1.setPrice(200);

        Product product2 = new Product();
        product2.setPrice(100);

        when(mapper.productDAOToProduct(productDAO1)).thenReturn(product1);
        when(mapper.productDAOToProduct(productDAO2)).thenReturn(product2);

        List<Product> result = service.getProductsSortedByPrice();

        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getPrice());
        assertEquals(200, result.get(1).getPrice());

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).productDAOToProduct(productDAO1);
        verify(mapper, times(1)).productDAOToProduct(productDAO2);
    }

    @Test
    void getProductsSortedByDescription_shouldReturnSortedListByDescription() {

        ProductDAO productDAO1 = new ProductDAO();
        productDAO1.setDescription("Cool Banana");

        ProductDAO productDAO2 = new ProductDAO();
        productDAO2.setDescription("Perfect Apple");

        List<ProductDAO> daoList = List.of(productDAO1, productDAO2);

        when(repository.findAll()).thenReturn(daoList);

        Product product1 = new Product();
        product1.setDescription("Cool Banana");

        Product product2 = new Product();
        product2.setDescription("Perfect Apple");

        when(mapper.productDAOToProduct(productDAO1)).thenReturn(product1);
        when(mapper.productDAOToProduct(productDAO2)).thenReturn(product2);

        List<Product> result = service.getProductsSortedByDescription();

        assertEquals(2, result.size());
        assertEquals("Cool Banana", result.get(0).getDescription());
        assertEquals("Perfect Apple", result.get(1).getDescription());

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).productDAOToProduct(productDAO1);
        verify(mapper, times(1)).productDAOToProduct(productDAO2);
    }

    @Test
    void getProductsSortedByCategory_shouldReturnListSortedByEnumOrder() {
        ProductDAO dao1 = new ProductDAO();
        dao1.setCategory(Category.CLOTHES);

        ProductDAO dao2 = new ProductDAO();
        dao2.setCategory(Category.ACCESSORIES);

        ProductDAO dao3 = new ProductDAO();
        dao3.setCategory(Category.SHOES);

        List<ProductDAO> daoList = List.of(dao1, dao2, dao3);
        when(repository.findAll()).thenReturn(daoList);

        Product product1 = new Product();
        product1.setCategory(Category.CLOTHES);

        Product product2 = new Product();
        product2.setCategory(Category.ACCESSORIES);

        Product product3 = new Product();
        product3.setCategory(Category.SHOES);

        when(mapper.productDAOToProduct(dao1)).thenReturn(product1);
        when(mapper.productDAOToProduct(dao2)).thenReturn(product2);
        when(mapper.productDAOToProduct(dao3)).thenReturn(product3);

        List<Product> sorted = service.getProductsSortedByCategory();

        assertEquals(Category.ACCESSORIES, sorted.get(0).getCategory());
        assertEquals(Category.CLOTHES, sorted.get(1).getCategory());
        assertEquals(Category.SHOES, sorted.get(2).getCategory());
    }

    @Test
    void createProduct_withValidProduct_shouldSaveAndReturnProduct() {

        Product inputProduct = new Product();
        inputProduct.setInitialQuantity(10);
        inputProduct.setQuantity(0);

        ProductDAO daoToSave = new ProductDAO();
        ProductDAO savedDAO = new ProductDAO();
        Product mappedProduct = new Product();

        when(validator.validate(inputProduct)).thenReturn(Collections.emptySet());
        when(mapper.productToDAO(inputProduct)).thenReturn(daoToSave);
        when(repository.save(daoToSave)).thenReturn(savedDAO);
        when(mapper.productDAOToProduct(savedDAO)).thenReturn(mappedProduct);


        Product result = service.createProduct(inputProduct);


        assertEquals(mappedProduct, result);
        assertEquals(inputProduct.getInitialQuantity(), inputProduct.getQuantity());
        verify(validator).validate(inputProduct);
        verify(repository).save(daoToSave);
        verify(mapper).productDAOToProduct(savedDAO);
    }

    @Test
    void createProduct_withInvalidProduct_shouldThrowValidationException() {

        Product invalidProduct = new Product();

        ConstraintViolation<Product> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<Product>> violations = Set.of(violation);

        when(validator.validate(invalidProduct)).thenReturn(violations);

        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> service.createProduct(invalidProduct)
        );

        assertEquals("Product info validation failed", exception.getMessage());
        assertEquals(violations, exception.getConstraintViolations());

        verify(validator).validate(invalidProduct);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void deleteProduct_shouldCallRepositoryWithCorrectId() {

        Long productId = 1L;
        service.deleteProduct(productId);
        verify(repository, times(1)).deleteById(productId);
    }

    @Test
    void findProductByName_shouldReturnMappedProductWhenFound() {

        String name = "Gadget";
        ProductDAO dao = new ProductDAO();
        Product mappedProduct = new Product();

        when(repository.findByName(name)).thenReturn(List.of(dao));
        when(mapper.productDAOToProduct(dao)).thenReturn(mappedProduct);


        Optional<Product> result = service.findProductByName(name);


        assertTrue(result.isPresent());
        assertEquals(mappedProduct, result.get());

        verify(repository).findByName(name);
        verify(mapper).productDAOToProduct(dao);
    }

    @Test
    void findProductByName_shouldReturnEmptyOptionalWhenNotFound() {

        String name = "Unknown";
        when(repository.findByName(name)).thenReturn(Collections.emptyList());


        Optional<Product> result = service.findProductByName(name);

        assertTrue(result.isEmpty());
        verify(repository).findByName(name);
        verifyNoInteractions(mapper);
    }

    @Test
    void findProductsByKeyword_shouldReturnMappedList() {

        String keyword = "modern";
        ProductDAO dao1 = new ProductDAO();
        ProductDAO dao2 = new ProductDAO();
        List<ProductDAO> daoList = List.of(dao1, dao2);

        Product product1 = new Product();
        Product product2 = new Product();

        when(repository.findByKeyword(keyword)).thenReturn(daoList);
        when(mapper.productDAOToProduct(dao1)).thenReturn(product1);
        when(mapper.productDAOToProduct(dao2)).thenReturn(product2);

        List<Product> result = service.findProductsByKeyword(keyword);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(product1, product2)));

        verify(repository).findByKeyword(keyword);
        verify(mapper, times(2)).productDAOToProduct(any(ProductDAO.class));
    }

    @Test
    void findProductsByKeyword_shouldReturnEmptyListWhenNoMatch() {

        String keyword = "nothing";
        when(repository.findByKeyword(keyword)).thenReturn(Collections.emptyList());

        List<Product> result = service.findProductsByKeyword(keyword);

        assertTrue(result.isEmpty());
        verify(repository).findByKeyword(keyword);
        verifyNoInteractions(mapper);
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct_whenValidAndExists() throws Exception {

        Product input = new Product();
        input.setId(1L);
        input.setPrice(100.0);
        input.setQuantity(5);
        input.setCategory(Category.ACCESSORIES);

        Product existing = new Product();
        existing.setId(1L);
        existing.setPrice(90.0);
        existing.setQuantity(2);
        existing.setCategory(Category.CLOTHES);

        ProductDAO daoToSave = new ProductDAO();
        ProductDAO daoSaved = new ProductDAO();
        Product mappedUpdated = new Product();

        when(validator.validate(input)).thenReturn(Collections.emptySet());
        doReturn(Optional.of(existing)).when(service).findProductById(1L);
        when(mapper.productToDAO(existing)).thenReturn(daoToSave);
        when(repository.save(daoToSave)).thenReturn(daoSaved);
        when(mapper.productDAOToProduct(daoSaved)).thenReturn(mappedUpdated);


        Product result = service.updateProduct(input);

        assertEquals(mappedUpdated, result);
        assertEquals(input.getPrice(), existing.getPrice());
        assertEquals(input.getQuantity(), existing.getQuantity());
        assertEquals(input.getCategory(), existing.getCategory());

        verify(repository).save(daoToSave);
        verify(mapper).productDAOToProduct(daoSaved);
    }

    @Test
    void updateProduct_shouldThrowValidationException_whenInvalid() {

        Product invalidProduct = new Product();

        ConstraintViolation<Product> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<Product>> violations = Set.of(violation);

        when(validator.validate(invalidProduct)).thenReturn(violations);


        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> service.updateProduct(invalidProduct)
        );

        assertEquals("Validation failed", exception.getMessage());
        verify(validator).validate(invalidProduct);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateProduct_shouldThrowNotFoundException_whenProductNotExists() throws Exception {

        Product input = createProduct();
        input.setId(999L);

        when(validator.validate(input)).thenReturn(Collections.emptySet());
        doReturn(Optional.empty()).when(service).findProductById(999L);
        assertThrows(
            ChangeSetPersister.NotFoundException.class,
            () -> service.updateProduct(input)
        );

        verify(validator).validate(input);
        verify(service).findProductById(999L);
    }
  /*  @Test
    void sellProductById_shouldDecreaseQuantity_whenQuantityGreaterThanZero() throws Exception {

        Long productId = 1L;
        Product product = createProduct();
        product.setQuantity(5);

        doReturn(Optional.of(product)).when(service).findProductById(productId);

        service.sellProductById(productId);


        assertEquals(4, product.getQuantity());
        verify(service).updateProductQuantity(product);
    } */
  /*  @Test
    void sellProductById_shouldThrowException_whenQuantityIsZero() throws Exception {

        Long productId = 2L;
        Product product = new Product();
        product.setId(productId);
        product.setQuantity(0);

        when(service.findProductById(productId)).thenReturn(Optional.of(product));

        InsufficientStockException ex = assertThrows(InsufficientStockException.class,
            () -> service.sellProductById(productId));

        assertEquals("Not enough quantity to sell", ex.getMessage());
        verify(service, never()).updateProductQuantity(any());
    } */
  /*  @Test
    void sellProductById_shouldThrowProductNotFoundException_whenProductNotFound() throws Exception {
        Long id = 99L;
        doReturn(Optional.empty()).when(service).findProductById(id);

        assertThrows(ProductNotFoundException.class, () -> service.sellProductById(id));
    } */

    @Test
    void sellProductById_success() {
        Product product = new Product();
        product.setId(existingProductId);
        product.setQuantity(5);

        when(service.findProductById(existingProductId)).thenReturn(Optional.of(product));

        service.sellProductById(existingProductId);

        assertEquals(4, product.getQuantity());
        verify(service).updateProductQuantity(product);
    }

    @Test
    void sellProductById_productNotFound() {
        when(repository.findById(missingProductId)).thenReturn(Optional.empty());

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.sellProductById(missingProductId));

        assertTrue(ex.getMessage().contains(missingProductId.toString()));
    }

    @Test
    void sellProductById_insufficientStock() {
        ProductDAO productDAO = new ProductDAO();
        productDAO.setId(existingProductId);

        Product product = new Product();
        product.setId(existingProductId);
        product.setQuantity(0);

        when(repository.findById(existingProductId)).thenReturn(Optional.of(productDAO));
        when(mapper.productDAOToProduct(productDAO)).thenReturn(product);
        InsufficientStockException ex = assertThrows(InsufficientStockException.class, () -> service.sellProductById(existingProductId));

        assertEquals("Not enough quantity to sell", ex.getMessage());
    }
    private List<ProductDAO> createProductDAOList() {
        List<ProductDAO> productDAOList = new ArrayList<>();
        productDAOList.add(productDAO);
        productDAOList.add(productDAO);
        return productDAOList;
    }

    private List<Product> createProductList() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product);
        return productList;
    }

}