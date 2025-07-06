package com.example.products.business.handlers;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long id) {
        super("Not enough quantity to sell");
    }
}
