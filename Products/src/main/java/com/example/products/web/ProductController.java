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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Optional;

@Api(tags = {DescriptionVariables.PRODUCT})

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Finds all products",
        notes = "Returns the entire list of products",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})

    @GetMapping("/")
    public String displayProductList(@RequestParam(required = false) String message,
                                     @RequestParam(required = false) String error,
                                     Model model) {
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", productService.getAllProducts());
        return "productList";
    }
    @GetMapping("/sort-product")
    @ApiOperation(value = "Finds all products sorted by name",
        notes = "Returns the entire list of products sorted by name",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public String displayProductListSortedByName(@RequestParam(required = false) String message,
                                                 @RequestParam(required = false) String error,
                                                 Model model) {
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", productService.getProductsSortedByName());
        return "productList";
    }
    @GetMapping("/sort-product-by-description")
    @ApiOperation(value = "Finds all products sorted by description",
        notes = "Returns the entire list of products sorted by description",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public String displayProductListSortedByDescription(@RequestParam(required = false) String message,
                                                        @RequestParam(required = false) String error,
                                                        Model model) {
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", productService.getProductsSortedByDescription());
        return "productList";
    }

    @GetMapping("/sort-product-by-category")
    @ApiOperation(value = "Finds all products sorted by category",
        notes = "Returns the entire list of products sorted by category",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public String displayProductListSortedByCategory(@RequestParam(required = false) String message,
                                                     @RequestParam(required = false) String error,
                                                     Model model) {
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", productService.getProductsSortedByCategory());
        return "productList";
    }

    @GetMapping("/sort-product-by-price")
    @ApiOperation(value = "Finds all products sorted by price",
        notes = "Returns the entire list of products sorted by price",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public String displayProductListSortedByPrice(@RequestParam(required = false) String message,
                                                  @RequestParam(required = false) String error,
                                                  Model model) {
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", productService.getProductsSortedByPrice());
        return "productList";
    }
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable() Long id) {
        try {
            productService.deleteProduct(id);
            return "redirect:/?message=PRODUCT_DELETED_SUCCESSFULLY";

        } catch (Exception exception) {
            return "redirect:/?message=PRODUCT_DELETE_FAILED&error=" + exception.getMessage();

        }
    }
    @GetMapping("/sell-product/{id}")
    public String sellProduct(@PathVariable Long id, Model model) {
        try {
            productService.sellProductById(id);
            return "redirect:/?message=PRODUCT_SOLD_SUCCESS";
        } catch (IllegalStateException e) {
            return "redirect:/?message=PRODUCT_SOLD_FAILED&error=" + e.getMessage();
        } catch (Exception e) {
            return "redirect:/?message=PRODUCT_SOLD_FAILED&error=Unexpected error occurred";
        }
    }
    @GetMapping("/add-product")
    @ApiOperation(value = "Shows the page to add new product",
        notes = "Shows the page to add new product",
        response = Product.class,
        responseContainer = "String")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public String displayAddProductPage() {
        return "addProduct";
    }

    @PostMapping("/add-product")
    @ApiOperation(value = "Saves the new product to the database",
        response = Product.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})

    public String createProduct(Product product) {
        try {
            this.productService.createProduct(product);
            return "redirect:/?message=PRODUCT_CREATED_SUCCESS";
        } catch (Exception exception) {
            return "redirect:/?message=PRODUCT_CREATION_FAILED&error=" + exception.getMessage();
        }

    }

    @GetMapping("/edit/{id}")
    public String showEditProductPage(@PathVariable Long id, Model model) {
        try {
            Optional<Product> foundProduct = productService.findProductById(id);
            model.addAttribute("productItem", foundProduct);
            return "editProduct";
        } catch (Exception exception) {
            return "redirect:/?message=PRODUCT_EDIT_FAILED&error=" + exception.getMessage();
        }
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Product product) {
        try {
            product.setId(id);
            productService.updateProduct(product);
            return "redirect:/?message=PRODUCT_EDITED_SUCCESSFULLY";
        } catch (Exception exception) {
            return "redirect:/?message=PRODUCT_EDIT_FAILED&error=" + exception.getMessage();
        }
    }

    @GetMapping(value = "filters")
    @ApiOperation(value = "Finds all products by name",
        notes = "Returns the entire list of products by keyword",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public String findProductByName(@RequestParam(value = "name", required = true) String name, Model model) {
        model.addAttribute("search", productService.findProductByName(name));
        return "filters";
    }

    @GetMapping(path = {"/search"})
    @ApiOperation(value = "Finds all products by keyword",
        notes = "Returns the entire list of products by keyword",
        response = Product.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
        @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})

    public String search(Product product, Model model, String keyword) {
        if (keyword != null) {
            List<Product> list = productService.findProductsByKeyword(keyword);
            model.addAttribute("productList", list);
        } else {
            List<Product> list = productService.getAllProducts();
            model.addAttribute("productList", list);
        }
        return "productList";
    }


}