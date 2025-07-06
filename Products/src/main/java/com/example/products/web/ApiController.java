package com.example.products.web;

import com.example.products.model.Product;
import com.example.products.business.service.ProductService;
import com.example.products.swagger.DescriptionVariables;
import com.example.products.swagger.HTMLResponseMessages;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@Api(tags = {DescriptionVariables.PRODUCT})
@RestController
@RequestMapping("/api/products")
public class ApiController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Finds all products",
        notes = "Returns the entire list of products",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @ApiOperation(value = "Finds all products sorted by name",
        notes = "Returns the entire list of products sorted by name",
        response = Product.class,
        responseContainer = "List")
    @GetMapping("/sorted-by-name")
    public List<Product> getProductsSortedByName() {
        return productService.getProductsSortedByName();
    }

    @ApiOperation(value = "Finds all products sorted by description",
        notes = "Returns the entire list of products sorted by description",
        response = Product.class,
        responseContainer = "List")
    @GetMapping("/sorted-by-description")
    public List<Product> getProductsSortedByDescription() {
        return productService.getProductsSortedByDescription();
    }

    @ApiOperation(value = "Finds all products sorted by category",
        notes = "Returns the entire list of products sorted by category",
        response = Product.class,
        responseContainer = "List")
    @GetMapping("/sorted-by-category")
    public List<Product> getProductsSortedByCategory() {
        return productService.getProductsSortedByCategory();
    }

    @ApiOperation(value = "Finds all products sorted by price",
        notes = "Returns the entire list of products sorted by price",
        response = Product.class,
        responseContainer = "List")
    @GetMapping("/sorted-by-price")
    public List<Product> getProductsSortedByPrice() {
        return productService.getProductsSortedByPrice();
    }

    @ApiOperation(value = "Deletes a product by ID")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @ApiOperation(value = "Sells a product by ID")
    @PostMapping("/sell/{id}")
    public void sellProduct(@PathVariable Long id) {
        productService.sellProductById(id);
    }
}


