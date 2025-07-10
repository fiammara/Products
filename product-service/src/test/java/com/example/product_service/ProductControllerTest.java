package com.example.product_service;


import com.example.product_service.business.service.ProductService;
import com.example.product_service.model.Product;
import com.example.product_service.web.ProductController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)

public class ProductControllerTest {
    public static String URL = "/products";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ProductService service;

    @Test
    void shouldReturnAllProductsSuccessfully() throws Exception {
        List<Product> productList = createProductList();

        when(service.getAllProducts()).thenReturn(productList);

        this.mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(productList.size()))
            .andExpect(jsonPath("$[0].name").value(productList.get(0).getName()));
    }

    @Test
    void shouldReturnProductWhenIdExists() throws Exception {
        Product mockProduct = createProduct();
        when(service.findProductById(1L)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(mockProduct.getId()))
            .andExpect(jsonPath("$.name").value(mockProduct.getName()))
            .andExpect(jsonPath("$.price").value(mockProduct.getPrice()));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        when(service.findProductById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        Product productToCreate = new Product();
        productToCreate.setName("Banana");
        productToCreate.setPrice(2.50);

        Product createdProduct = new Product();
        createdProduct.setId(1L);
        createdProduct.setName("Banana");
        createdProduct.setPrice(2.50);

        when(service.createProduct(any(Product.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/products/add-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productToCreate)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdProduct.getId()))
            .andExpect(jsonPath("$.name").value(createdProduct.getName()))
            .andExpect(jsonPath("$.price").value(createdProduct.getPrice()));
    }

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        doNothing().when(service).deleteProduct(1L);

        mockMvc.perform(delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product createdProduct = new Product();
        createdProduct.setId(1L);
        createdProduct.setName("MN");
        createdProduct.setDescription("Modern");
        createdProduct.setPrice(250);

        String json = objectMapper.writeValueAsString(createdProduct);
        when(service.findProductById(anyLong())).thenReturn(Optional.of(createdProduct));

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/products/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        verify(service, times(1)).updateProduct(argThat(p -> p.getId().equals(1L)));

    }

    private List<Product> createProductList() {
        List<Product> productList = new ArrayList<>();
        Product product1 = createProduct();
        Product product2 = createProduct();
        productList.add(product1);
        productList.add(product2);
        return productList;
    }

    private Product createProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Nike");

        return product;
    }

}
