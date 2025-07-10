package com.example.product_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Schema(description = "Model of product data ")
//@Component
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {

    @Schema(description = "The unique id of the product")
    private Long id;
    @Schema(description = "The name of product")

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;

    private int quantity;

    private int initial_quantity;
    @NotNull
    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;

}
