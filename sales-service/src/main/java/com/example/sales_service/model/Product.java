package com.example.sales_service.model;

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
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {

    @Schema(description = "The unique id of the product")
    private Long id;
    @Schema(description = "The name of product")

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
    @NotNull
    @NotEmpty
    @NotBlank
    private String description;
    @NotNull
    @NotEmpty
    @NotBlank
    private int quantity;
    @NotNull
    @NotEmpty
    @NotBlank
    private int initial_quantity;
    @NotNull
    @NotEmpty
    @NotBlank
    private double price;
    @NotNull
    @NotEmpty
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Category category;

}
