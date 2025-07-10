package com.example.products.web;


import com.example.products.model.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;



@Controller
public class UIController {


    private final WebClient webClient;

    public UIController(WebClient.Builder webClientBuilder) {
            this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();

    }


    @GetMapping("/")
    public String displayProductList(@RequestParam(required = false) String message,
                                     @RequestParam(required = false) String error,
                                     Model model) {

        List<Product> productList = webClient.get()
            .uri("/products")
            .retrieve()
            .bodyToFlux(Product.class)
            .collectList()
            .block();


        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", productList);
        return "productList";
    }

    @GetMapping("/sort-product")
    public String displayProductListSortedByName(@RequestParam(required = false) String message,
                                                 @RequestParam(required = false) String error,
                                                 Model model) {


        List<Product> sortedList = new ArrayList<>();
        String errorMessage = null;

        try {
            sortedList = webClient.get()
                .uri("http://localhost:8081/products/sorted-by-name")
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();
        } catch (WebClientResponseException e) {
            // Handles non-2xx HTTP status codes
            errorMessage = "Server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
            e.printStackTrace();
        } catch (WebClientRequestException e) {
            // Handles network errors, like connection refused
            errorMessage = "Connection error: " + e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            // Catch-all for any other exceptions
            errorMessage = "Unexpected error occurred: " + e.getMessage();
            e.printStackTrace();
        }

        model.addAttribute("message", message);
        model.addAttribute("error", error != null ? error : errorMessage);
        model.addAttribute("productList", sortedList);

        return "productList";
    }

    @GetMapping("/sort-product-by-description")


    public String displayProductListSortedByDescription(@RequestParam(required = false) String message,
                                                        @RequestParam(required = false) String error,
                                                        Model model) {

        List<Product> sortedList = webClient.get()
            .uri("http://localhost:8081/products/sorted-by-description")
            .retrieve()
            .bodyToFlux(Product.class)
            .collectList()
            .block();
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", sortedList);
        return "productList";
    }

    @GetMapping("/sort-product-by-category")

    public String displayProductListSortedByCategory(@RequestParam(required = false) String message,
                                                     @RequestParam(required = false) String error,
                                                     Model model) {
        List<Product> sortedList = webClient.get()
            .uri("http://localhost:8081/products/sort-product-by-category")
            .retrieve()
            .bodyToFlux(Product.class)
            .collectList()
            .block();
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", sortedList);
        return "productList";
    }

    @GetMapping("/sort-product-by-price")

    public String displayProductListSortedByPrice(@RequestParam(required = false) String message,
                                                  @RequestParam(required = false) String error,
                                                  Model model) {
        List<Product> sortedList = webClient.get()
            .uri("http://localhost:8081/products/sort-product-by-price")
            .retrieve()
            .bodyToFlux(Product.class)
            .collectList()
            .block();
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("productList", sortedList);
        return "productList";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable() Long id) {


        try {

            webClient.delete()
                .uri("http://localhost:8081/products/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

            return "redirect:/?message=PRODUCT_DELETED_SUCCESSFULLY";

        } catch (WebClientResponseException e) {
            String encodedError = URLEncoder.encode(e.getResponseBodyAsString(), StandardCharsets.UTF_8);
            return "redirect:/?message=PRODUCT_DELETE_FAILED&error=" + encodedError;
        } catch (Exception e) {
            return "redirect:/?message=PRODUCT_DELETE_FAILED&error=Unexpected error occurred";
        }
    }

    @GetMapping("/sell-product/{id}")
    public String sellProduct(@PathVariable Long id) {
        try {
            webClient.post()
                .uri("/products/sell/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

            return "redirect:/?message=PRODUCT_SOLD_SUCCESS";
        } catch (WebClientResponseException e) {
            return "redirect:/?message=PRODUCT_SOLD_FAILED&error=" + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "redirect:/?message=PRODUCT_SOLD_FAILED&error=Unexpected error occurred";
        }
    }

    @GetMapping("/add-product")
    public String displayAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }


    @PostMapping("/add-product")
    public String createProduct(@ModelAttribute Product product) {
        try {
            webClient.post()
                .uri("http://localhost:8081/products/add-product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
            return "redirect:/?message=PRODUCT_CREATED_SUCCESS";
        } catch (WebClientResponseException e) {
            return "redirect:/add-product?error=" + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "redirect:/add-product?error=Unexpected error occurred";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditProductPage(@PathVariable Long id, Model model) {
        try {
            Product product = webClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .block();

            model.addAttribute("productItem", product);
            return "editProduct";
        } catch (WebClientResponseException e) {
            return "redirect:/?message=PRODUCT_EDIT_FAILED&error=" + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "redirect:/?message=PRODUCT_EDIT_FAILED&error=Unexpected error occurred";
        }
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Product product) {
        try {
            product.setId(id);

            webClient.put()
                .uri("http://localhost:8081/products/products/{id}", id)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

            return "redirect:/?message=PRODUCT_EDITED_SUCCESSFULLY";
        } catch (WebClientResponseException e) {
            return "redirect:/edit/" + id + "?error=" + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "redirect:/edit/" + id + "?error=Unexpected error occurred";
        }
    }

  /*  @GetMapping(value = "filters")

    public String findProductByName(@RequestParam(value = "name", required = true) String name, Model model) {
        try {
            List<Product> products = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/products/search")
                    .queryParam("name", name)
                    .build())
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();

            model.addAttribute("search", products);
            return "filters";
        } catch (WebClientResponseException e) {
            model.addAttribute("error", "Error fetching products: " + e.getResponseBodyAsString());
            return "filters";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error occurred");
            return "filters";
        }
    }
*/
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, Model model) {

        try {
            List<Product> productList = webClient.get()
                .uri("http://localhost:8081/products/search?keyword={keyword}", keyword)
                    .retrieve()
                    .bodyToFlux(Product.class)
                    .collectList()
                    .block();

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

}