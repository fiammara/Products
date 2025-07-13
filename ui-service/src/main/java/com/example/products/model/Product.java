package com.example.products.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {

    private Long id;


    private String name;

    private String description;

    private int quantity;

    private int initialQuantity;

    private double price;

    private Category category;

}