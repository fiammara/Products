package com.example.product_service.business.comparators;


import com.example.product_service.model.Product;

import java.util.Comparator;

public class ProductComparatorByQuantity implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {

        return (int) (o1.getPrice()-(o2.getPrice()));

    }
}
