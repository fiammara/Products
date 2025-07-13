package com.example.product_service.web;


import com.example.product_service.business.service.ProductService;
import com.example.product_service.model.Product;
import com.example.product_service.swagger.DescriptionVariables;
import com.example.product_service.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

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
    public List<Product> getAllProducts() {
        return productService.getAllProducts();

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
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws Exception {
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
    public List<Product> getProductsSortedByName() {

        return productService.getProductsSortedByName();
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
    public List<Product> getProductsSortedByDescription() {

        return productService.getProductsSortedByDescription();
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
    public List<Product> getProductsSortedByCategory() {
        return productService.getProductsSortedByCategory();
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

    public List<Product> getProductsSortedByPrice() {
        return productService.getProductsSortedByPrice();
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
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    /*
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
    public ResponseEntity<Product> createProduct(@RequestBody Product product) throws Exception {

        Product created = productService.createProduct(product);
        return ResponseEntity.ok(created);
    }
    @ApiOperation(value = "Update an existing product",
        notes = "Updates the product identified by the given ID with the provided data.")
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 401, message = HTMLResponseMessages.HTTP_401),
        @ApiResponse(code = 403, message = HTMLResponseMessages.HTTP_403),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody Product product) throws Exception {

        product.setId(id);
        productService.updateProduct(product);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<Product> findProductsByKeyword(@RequestParam String keyword) {

        return productService.findProductsByKeyword(keyword);
    }

*/

}
