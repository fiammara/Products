package com.example.sales_service.web;


import com.example.sales_service.business.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class SalesController {
    @Autowired
    private SalesService productService;


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
    /*  @GetMapping("/sell-product/{id}")
    public String sellProduct(@PathVariable Long id) {
        try {
            webClient.post()
                .uri("http://localhost:8081/api/sales/sell/" + id)
                .retrieve()
                .toBodilessEntity()
                .block();

            return "redirect:/?message=PRODUCT_SOLD_SUCCESS";

        } catch (WebClientResponseException e) {
            return "redirect:/?message=PRODUCT_SOLD_FAILED&error=" + e.getResponseBodyAsString();

        } catch (Exception e) {
            return "redirect:/?message=PRODUCT_SOLD_FAILED&error=Unexpected error occurred";
        }
    } */
/*
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
    } */

}
