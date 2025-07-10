package com.example.sales_service.business.handlers;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long id) {
        super("Not enough quantity to sell");
    }
}
