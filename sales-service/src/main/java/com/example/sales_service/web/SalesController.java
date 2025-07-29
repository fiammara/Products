package com.example.sales_service.web;


import com.example.sales_service.business.service.SalesService;
import com.example.sales_service.swagger.DescriptionVariables;
import com.example.sales_service.swagger.HTMLResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Tag(name = DescriptionVariables.SALES)
@RestController
@RequestMapping("/api/sales")
public class SalesController {
    @Autowired
    private SalesService salesService;

    @Operation(
        summary = "Selling products",
        description = "Performs a product sell operation"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200),
        @ApiResponse(responseCode = "409", description = HTMLResponseMessages.HTTP_409),
        @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500)
    })
    @PostMapping("/sell-product/{id}")
    public ResponseEntity<Object> sellProduct(@PathVariable Long id) {

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
