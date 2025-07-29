package com.example.product_service.web;


import com.example.product_service.business.service.ProductService;
import com.example.product_service.model.Product;
import com.example.product_service.swagger.DescriptionVariables;
import com.example.product_service.swagger.HTMLResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

@Tag(name = DescriptionVariables.PRODUCT)
@RestController
@Log4j2
@Validated
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(
        summary = "Find product by ID",
        description = "Returns a product for the given ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200),
        @ApiResponse(responseCode = "401", description = HTMLResponseMessages.HTTP_401),
        @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403),
        @ApiResponse(responseCode = "404", description = HTMLResponseMessages.HTTP_404),
        @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<Product> getProductById(
        @Parameter(description = "ID of the product to retrieve", required = true)
        @PathVariable Long id) {

        log.info("Request received: Get product by ID {}", id);

        Optional<Product> product = productService.findProductById(id);
        return product.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
        summary = "List all products",
        description = "Supports optional sorting by name, category, price or description."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200),
        @ApiResponse(responseCode = "204", description = HTMLResponseMessages.HTTP_204)
    })
    public ResponseEntity<List<Product>> getAllProducts(
        @Parameter(description = "Sort by field (name, category, price, description)", required = false)
        @RequestParam(value = "sortBy", required = false) String sortBy) {

        log.info("Fetching all products with sort: {}", sortBy);
        List<Product> products = productService.getProductsSorted(sortBy);
        List<String> allowedSortFields = List.of("name", "category", "price", "description");
        if (sortBy != null && !allowedSortFields.contains(sortBy)) {
            return ResponseEntity.badRequest().build();
        }
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Deletes a product by ID", description = "Deletes the product identified by the given ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = HTMLResponseMessages.HTTP_204),
        @ApiResponse(responseCode = "401", description = HTMLResponseMessages.HTTP_401),
        @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403),
        @ApiResponse(responseCode = "404", description = HTMLResponseMessages.HTTP_404),
        @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
        @Parameter(description = "ID of the product to delete", required = true)
        @PathVariable Long id) {
        log.info("Delete product request received for id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-product")
    @Operation(
        summary = "Create a new product",
        description = "Creates a new product and returns the created entity."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = HTMLResponseMessages.HTTP_201),
        @ApiResponse(responseCode = "400", description = HTMLResponseMessages.HTTP_400),
        @ApiResponse(responseCode = "401", description = HTMLResponseMessages.HTTP_401),
        @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403),
        @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Product> createProduct(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product object to create",
            required = true
        )
        @Valid @RequestBody Product product) {

        log.info("Request received to create a new product: {}", product);
        Product created = productService.createProduct(product);
        log.info("Product created successfully with ID: {}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    @Operation(
        summary = "Update an existing product",
        description = "Updates the product identified by the given ID with the provided data."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200),
        @ApiResponse(responseCode = "400", description = HTMLResponseMessages.HTTP_400),
        @ApiResponse(responseCode = "404", description = HTMLResponseMessages.HTTP_404),
        @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Product> updateProductById(
        @Parameter(description = "ID of the product to update", required = true)
        @NotNull @PathVariable Long id,

        @Parameter(description = "Updated product object", required = true)
        @Valid @RequestBody Product product) {

        log.info("Update product request received for id: {}", id);
        if (!id.equals(product.getId())) {
            log.warn("Product for update with id {} is not matching", id);
            return ResponseEntity.badRequest().build();
        }
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search products by keyword",
        description = "Returns products whose name or description contains the given keyword"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200),
        @ApiResponse(responseCode = "204", description = HTMLResponseMessages.HTTP_204),
        @ApiResponse(responseCode = "401", description = HTMLResponseMessages.HTTP_401),
        @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403),
        @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<Product>> findProductsByKeyword(
        @Parameter(description = "Keyword to search in product name or description", required = true, example = "shoes")
        @RequestParam String keyword) {

        log.info("Searching products with keyword: {}", keyword);

        List<Product> results = productService.findProductsByKeyword(keyword);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }
}