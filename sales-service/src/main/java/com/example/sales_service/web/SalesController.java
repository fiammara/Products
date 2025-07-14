package com.example.sales_service.web;


import com.example.sales_service.business.service.SalesService;
import com.example.sales_service.model.Product;
import com.example.sales_service.swagger.DescriptionVariables;
import com.example.sales_service.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Api(tags = {DescriptionVariables.SALES})
@RestController
@RequestMapping("/api/sales")
public class SalesController {
    @Autowired
    private SalesService salesService;

    @ApiOperation(value = "Selling products",
        notes = "Performs a product sell operation ",
        response = Void.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
        @ApiResponse(code = 409, message = HTMLResponseMessages.HTTP_409),
        @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @PostMapping("/sell-product/{id}")
    public ResponseEntity<Object> sellProduct(@PathVariable Long id) {
        System.out.println(2);
        try {
            salesService.sellProductById(id);

            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            Map<String, String> errorBody = Map.of("error", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorBody);
        } catch (Exception e) {
            Map<String, String> errorBody = Map.of("error", "Unexpected error");
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody);
        }

    }
}
