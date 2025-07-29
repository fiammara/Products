package com.example.products.web;


import com.example.products.web.webclient.ProductService;
import com.example.products.web.webclient.SalesService;
import com.example.products.UserSession;
import com.example.products.model.Product;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UIController {

    private final ProductService productService;
    private final SalesService salesService;
    private final UserSession userSession;

    public UIController(ProductService productService, SalesService salesService, UserSession userSession) {
        this.productService = productService;
        this.salesService = salesService;
        this.userSession = userSession;
    }

    private static final Logger logger = LoggerFactory.getLogger(UIController.class);

    @GetMapping("/")
    public String displayProductList(Model model, HttpServletResponse response) {
        logger.info("GET / called by user: {}", userSession.getUsername());
        List<Product> products = productService.getAllProducts();

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        model.addAttribute("productList", products);

        String username = userSession.getUsername();
        if (username != null) {
            model.addAttribute("username", username);
        }

        return "productList";
    }

    @GetMapping("/products")
    public String displayProductListSorted(@RequestParam(required = false) String sortBy,
                                           @RequestParam(required = false) String message,
                                           @RequestParam(required = false) String error,
                                           Model model) {

        List<Product> products = productService.getProductsSorted(sortBy);
        model.addAttribute("productList", products);
        model.addAttribute("message", message);
        model.addAttribute("error", error);

        return "productList";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "PRODUCT_DELETED_SUCCESSFULLY");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "PRODUCT_DELETE_FAILED");
            return "redirect:/";
        }
    }

    @PostMapping("/sell-product/{id}")
    public String sellProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Selling product with id: {}", id);
        try {
            salesService.sellProduct(id);
            redirectAttributes.addFlashAttribute("message", "PRODUCT_SOLD_SUCCESS");
            return "redirect:/";
        } catch (WebClientResponseException e) {
            String errorMessage = extractErrorMessage(e.getResponseBodyAsString());
            logger.error("Error selling product: {}", errorMessage, e);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Unexpected error selling product: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/add-product")
    public String displayAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @PostMapping("/add-product")
    public String createProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        logger.info("Adding new product: {}", product);
        try {
            productService.addProduct(product);
            logger.info("Product added successfully: {}", product.getId());
            redirectAttributes.addFlashAttribute("message", "PRODUCT_CREATED_SUCCESS");
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error adding product: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to add product");
            return "redirect:/add-product";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditProductPage(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        if (!userSession.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Please login first");
            return "redirect:/login";
        }
        try {
            String jwt = userSession.getJwtToken();
            Product product = productService.findProductById(id, jwt);

            model.addAttribute("productItem", product);
            return "editProduct";
        } catch (Exception e) {
            logger.error("Error loading product for edit: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "PRODUCT_EDIT_FAILED");
            return "redirect:/";
        }
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Product product, RedirectAttributes redirectAttributes) {
        try {
            String jwt = userSession.getJwtToken();
            if (!userSession.isAuthenticated()) {
                redirectAttributes.addFlashAttribute("error", "Please login first");
                return "redirect:/login";
            }

            product.setId(id);
            productService.updateProduct(id, product, jwt);

            redirectAttributes.addFlashAttribute("message", "PRODUCT_EDITED_SUCCESSFULLY");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred");
            return "redirect:/edit/" + id;
        }
    }


    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, Model model) {

        try {
            List<Product> productList = productService.search(keyword);
            model.addAttribute("productList", productList);
            model.addAttribute("keyword", keyword);
            return "productList";

        } catch (WebClientResponseException e) {
            model.addAttribute("error", "Error fetching products: " + e.getResponseBodyAsString());
            return "productList";

        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error occurred");
            return "productList";
        }
    }

    private String extractErrorMessage(String responseBody) {
        try {
            int start = responseBody.indexOf(":\"") + 2;
            int end = responseBody.indexOf("\"", start);
            return responseBody.substring(start, end);
        } catch (Exception e) {
            return "Unknown error occurred";
        }
    }
}

