package com.example.product_service.business.comparators;


import com.example.product_service.model.Product;

import java.util.Comparator;

public class ProductComparatorByDescription implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return   o1.getDescription().compareTo(o2.getDescription());

    }
}
