package com.example.product_service.web;


import com.example.product_service.business.service.ProductService;
import com.example.product_service.model.Product;
import com.example.product_service.swagger.DescriptionVariables;
import com.example.product_service.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(tags = {DescriptionVariables.PRODUCT})
@RestController
@Log4j2
@Validated
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Finds all products",
        notes = "Returns the entire list of products",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Request received: Get all products");

        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            log.info("No products found");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Find product by ID",
        notes = "Returns a product for the given ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
        @ApiParam(value = "ID of the product to retrieve", required = true)
        @PathVariable Long id) throws Exception {
        log.info("Request received: Get product by ID {}", id);
        Optional<Product> product = productService.findProductById(id);
        return product.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Finds all products sorted by name",
        notes = "Returns the entire list of products sorted by name",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/sorted-by-name")
    public ResponseEntity<List<Product>> getProductsSortedByName() {
        log.info("Request received: Get products sorted by name");

        List<Product> products = productService.getProductsSortedByName();

        if (products.isEmpty()) {
            log.info("No products found for sorting by name");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Finds all products sorted by description",
        notes = "Returns the entire list of products sorted by description",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/sorted-by-description")
    public ResponseEntity<List<Product>> getProductsSortedByDescription() {
        log.info("Request received: Get products sorted by description");

        List<Product> products = productService.getProductsSortedByDescription();

        if (products.isEmpty()) {
            log.info("No products found for sorting by description");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Finds all products sorted by category",
        notes = "Returns the entire list of products sorted by category",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/sort-product-by-category")
    public ResponseEntity<List<Product>> getProductsSortedByCategory() {
        log.info("Request received: Get products sorted by category");

        List<Product> products = productService.getProductsSortedByCategory();

        if (products.isEmpty()) {
            log.info("No products found for sorting by category");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Finds all products sorted by price",
        notes = "Returns the entire list of products sorted by price",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/sort-product-by-price")
    public ResponseEntity<List<Product>> getProductsSortedByPrice() {
        log.info("Request received: Get products sorted by price");

        List<Product> products = productService.getProductsSortedByPrice();

        if (products.isEmpty()) {
            log.info("No products found for sorting by price");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Deletes a product by ID",
        notes = "Deletes the product identified by the given ID.")
    @ApiResponses({
        @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
        @ApiParam(value = "ID of the product to delete", required = true)
        @PathVariable Long id) {
        log.info("Delete product request received for id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Create a new product",
        notes = "Creates a new product and returns the created entity.")
    @ApiResponses({
        @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @PostMapping("/add-product")
    public ResponseEntity<Product> createProduct(
        @ApiParam(value = "Product object to create", required = true)
        @Valid @RequestBody Product product) throws Exception {
        log.info("Request received to create a new product: {}", product);
        Product created = productService.createProduct(product);
        log.info("Product created successfully with ID: {}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @SneakyThrows
    @PutMapping("/products/{id}")
    @ApiOperation(value = "Update an existing product",
        notes = "Updates the product identified by the given ID with the provided data.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})

    public ResponseEntity<Product> updateProductById(
        @ApiParam(value = "ID of the product to update", required = true)
        @NotNull @PathVariable Long id,
        @ApiParam(value = "Updated product object", required = true)
        @Valid @RequestBody Product product) {
        log.info("Update product request received for id: {}", id);
        if (!id.equals(product.getId())) {
            log.warn("Product for update with id {} is not matching", id);
            return ResponseEntity.badRequest().build();
        }
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @ApiOperation(value = "Search products by keyword",
        notes = "Returns products whose name or description contains the given keyword")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> findProductsByKeyword(
        @ApiParam(value = "Keyword to search in product name or description", required = true, example = "shoes")
        @RequestParam String keyword) {
        log.info("Searching products with keyword: {}", keyword);
        List<Product> results = productService.findProductsByKeyword(keyword);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }
}