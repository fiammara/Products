package com.example.products;

import com.example.products.business.service.ProductService;
import com.example.products.model.Product;
import com.example.products.web.ProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(ProductController.class)

public class ProductControllerTest {
    public static String URL = "/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;
    @Test
    void findAllProducts_shouldReturnViewWithProducts() throws Exception {
        List<Product> productList = createProductList();

        when(service.getAllProducts()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andExpect(status().isOk())
            .andExpect(view().name("productList"))
            .andExpect(model().attributeExists("productList"))
            .andExpect(model().attribute("productList", productList));

        verify(service, times(1)).getAllProducts();
    }
    @Test
    void displayProductListSortedByName_shouldReturnSortedProductListAndView() throws Exception {

        List<Product> sortedProducts = createProductList();

        when(service.getProductsSortedByName()).thenReturn(sortedProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/sort-product")

                .param("message", "Test Message")
                .param("error", "Test Error"))
            .andExpect(status().isOk())
            .andExpect(view().name("productList"))
            .andExpect(model().attributeExists("message"))
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attribute("message", "Test Message"))
            .andExpect(model().attribute("error", "Test Error"))
            .andExpect(model().attribute("productList", sortedProducts));

        verify(service, times(1)).getProductsSortedByName();
    }
    @Test
    void displayProductListSortedByDescription_shouldReturnSortedProductListAndView() throws Exception {

        List<Product> sortedProducts = createProductList();

        when(service.getProductsSortedByDescription()).thenReturn(sortedProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/sort-product-by-description")
                .param("message", "Test Message")
                .param("error", "Test Error"))
            .andExpect(status().isOk())
            .andExpect(view().name("productList"))
            .andExpect(model().attributeExists("message"))
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attribute("message", "Test Message"))
            .andExpect(model().attribute("error", "Test Error"))
            .andExpect(model().attribute("productList", sortedProducts));

        verify(service, times(1)).getProductsSortedByDescription();
    }
    @Test
    void displayProductListSortedByCategory_shouldReturnSortedProductListAndView() throws Exception {
        List<Product> sortedProducts = createProductList();

        when(service.getProductsSortedByCategory()).thenReturn(sortedProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/sort-product-by-category")
                .param("message", "Test Message")
                .param("error", "Test Error"))
            .andExpect(status().isOk())
            .andExpect(view().name("productList"))
            .andExpect(model().attributeExists("message"))
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attribute("message", "Test Message"))
            .andExpect(model().attribute("error", "Test Error"))
            .andExpect(model().attribute("productList", sortedProducts));

        verify(service, times(1)).getProductsSortedByCategory();

    }
    @Test
    void displayProductListSortedByPrice_shouldReturnSortedProductListAndView() throws Exception {

        List<Product> sortedProducts = createProductList();

        when(service.getProductsSortedByPrice()).thenReturn(sortedProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/sort-product-by-price")
                .param("message", "Test Message")
                .param("error", "Test Error"))
            .andExpect(status().isOk())
            .andExpect(view().name("productList"))
            .andExpect(model().attributeExists("message"))
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attribute("message", "Test Message"))
            .andExpect(model().attribute("error", "Test Error"))
            .andExpect(model().attribute("productList", sortedProducts));

        verify(service, times(1)).getProductsSortedByPrice();
    }
    @Test
    void displayAddProductPage_shouldReturnAddProductView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/add-product"))
            .andExpect(status().isOk())
            .andExpect(view().name("addProduct"));
    }
    @Test
    void createProduct_success_shouldRedirectWithSuccessMessage() throws Exception {
        // Given
        Product product = new Product();
        product.setName("Test Product");
        // ... set other required fields on product as needed

        // When service.createProduct is called, do nothing (or return the product)
        when(service.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-product")
                .flashAttr("product", product))  // send product as form data
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/?message=PRODUCT_CREATED_SUCCESS"));

        verify(service, times(1)).createProduct(any(Product.class));
    }

    @Test
    void createProduct_failure_shouldRedirectWithErrorMessage() throws Exception {
        Product product = new Product();
        product.setName("Test Product");

        when(service.createProduct(any(Product.class)))
            .thenThrow(new RuntimeException("Creation failed"));

        mockMvc.perform(MockMvcRequestBuilders.post("/add-product")
                .flashAttr("product", product))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/?message=PRODUCT_CREATION_FAILED&error=*"));

        verify(service, times(1)).createProduct(any(Product.class));
    }
    @Test
    void deleteProduct_success_shouldRedirectWithSuccessMessage() throws Exception {
        Long productId = 1L;

        // Do nothing when deleteProduct is called (void method)
        doNothing().when(service).deleteProduct(productId);

        mockMvc.perform(MockMvcRequestBuilders.get("/delete/{id}", productId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/?message=PRODUCT_DELETED_SUCCESSFULLY"));

        verify(service, times(1)).deleteProduct(productId);
    }

    @Test
    void deleteProduct_failure_shouldRedirectWithErrorMessage() throws Exception {
        Long productId = 1L;

        doThrow(new RuntimeException("Delete failed")).when(service).deleteProduct(productId);

        mockMvc.perform(MockMvcRequestBuilders.get("/delete/{id}", productId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/?message=PRODUCT_DELETE_FAILED&error=*"));

        verify(service, times(1)).deleteProduct(productId);}


    @Test
    void sellProduct_success_shouldRedirectWithSuccessMessage() throws Exception {
        Long productId = 1L;

        // Simulate successful sellProductById call (void method)
        doNothing().when(service).sellProductById(productId);

        mockMvc.perform(MockMvcRequestBuilders.get("/sell-product/{id}", productId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/?message=PRODUCT_SOLD_SUCCESS"));

        verify(service, times(1)).sellProductById(productId);
    }

    @Test
    void sellProduct_insufficientQuantity_shouldRedirectWithErrorMessage() throws Exception {
        Long productId = 1L;

        doThrow(new IllegalStateException("Not enough quantity to sell")).when(service).sellProductById(productId);

        mockMvc.perform(MockMvcRequestBuilders.get("/sell-product/{id}", productId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/?message=PRODUCT_SOLD_FAILED&error=Not enough quantity to sell"));

        verify(service, times(1)).sellProductById(productId);
    }
    @Test
    void sellProduct_otherException_shouldRedirectWithGenericError() throws Exception {
        Long productId = 1L;

        // Simulate generic exception thrown
        doThrow(new RuntimeException("Some error")).when(service).sellProductById(productId);

        mockMvc.perform(MockMvcRequestBuilders.get("/sell-product/{id}", productId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/?message=PRODUCT_SOLD_FAILED&error=Unexpected error occurred"));

        verify(service, times(1)).sellProductById(productId);
    }


    @Test
    void showEditProductPage_whenException_shouldRedirectWithError() throws Exception {
        Long productId = 1L;

        when(service.findProductById(productId)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/edit/{id}", productId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/?message=PRODUCT_EDIT_FAILED&error=Error"));

        verify(service, times(1)).findProductById(productId);}
  /*  @Test
    void findProductByName_shouldReturnFiltersViewWithSearchResults() throws Exception {
        String searchName = "Nike";
        Product product = new Product();
        product.setName(searchName);

        when(service.findProductByName(searchName)).thenReturn(Optional.of(product));
        mockMvc.perform(MockMvcRequestBuilders.get("/filters")
                .param("name", searchName))
            .andExpect(status().isOk())
            .andExpect(view().name("filters"))
            .andExpect(model().attributeExists("search"))
            .andExpect(model().attribute("search", Optional.of(product)));

        verify(service, times(1)).findProductByName(searchName);
    } */


    private List<Product> createProductList() {
        List<Product> trainingList = new ArrayList<>();
        Product product1 = createProduct();
        Product product2 = createProduct();
        trainingList.add(product1);
        trainingList.add(product2);
        return trainingList;
    }
    private Product createProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Nike");

        return product;
    }

}
