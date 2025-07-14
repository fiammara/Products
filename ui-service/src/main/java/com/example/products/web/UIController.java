package com.example.products.web;


import com.example.products.model.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import reactor.core.publisher.Mono;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class UIController {

    @Value("${PRODUCT_SERVICE_URL}")
    private String productServiceUrl;
    private final WebClient productClient;

    @Value("${SALES_SERVICE_URL}")
    private String salesServiceUrl;
    private final WebClient salesClient;

    public UIController(
        @Qualifier("productClient") WebClient productClient,
        @Qualifier("salesClient") WebClient salesClient
    ) {
        this.productClient = productClient;
        this.salesClient = salesClient;
    }

    @GetMapping("/")
    public Mono<String> displayProductList(Model model) {
        return productClient.get()
            .uri(productServiceUrl)
            .retrieve()
            .bodyToFlux(Product.class)
            .collectList()
            .doOnError(e -> {

                System.err.println("Error fetching products: " + e.getMessage());
            })
            .onErrorReturn(Collections.emptyList())
            .map(products -> {
                model.addAttribute("productList", products);
                return "productList";
            });
    }



    @GetMapping("/sort-product")
    public String displayProductListSortedByName(@RequestParam(required = false) String message,
                                                 @RequestParam(required = false) String error,
                                                 Model model) {


        List<Product> sortedList = new ArrayList<>();
        String errorMessage = null;

        try {
            sortedList = productClient.get()
                .uri(productServiceUrl + "/sorted-by-name")
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

        List<Product> sortedList = productClient.get()
            .uri(productServiceUrl + "/sorted-by-description")
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
        List<Product> sortedList = productClient.get()
            .uri(productServiceUrl + "/sort-product-by-category")
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
        List<Product> sortedList = productClient.get()
            .uri(productServiceUrl + "/sort-product-by-price")
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

            productClient.delete()
                .uri(productServiceUrl + "/{id}", id)
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

            salesClient.post()
                .uri(salesServiceUrl + "/sell-product/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();


        } catch (WebClientResponseException e) {

            String errorMessage;
            try {
                errorMessage = e.getResponseBodyAsString();

                int start = errorMessage.indexOf(":\"") + 2;
                int end = errorMessage.indexOf("\"", start);
                errorMessage = errorMessage.substring(start, end);
            } catch (Exception parseEx) {
                errorMessage = "Unknown error occurred";
            }


        } catch (Exception e) {

        }

        return "redirect:/";
    }

    @GetMapping("/add-product")
    public String displayAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }


    @PostMapping("/add-product")
    public String createProduct(@ModelAttribute Product product) {
        try {
            productClient.post()
                .uri(productServiceUrl + "/add-product")
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
            Product product = productClient.get()
                .uri(productServiceUrl + "{id}", id)
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

            productClient.put()
                .uri(productServiceUrl + "/products/{id}", id)
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


    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, Model model) {

        try {
            List<Product> productList = productClient.get()
                .uri(productServiceUrl + "/search?keyword={keyword}", keyword)
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

