package com.springproject.orderservice.exception;

public class ProductNotInStockException  extends RuntimeException  {

    public ProductNotInStockException() {
        super("Product is not in stock");
    }

    public ProductNotInStockException(String message) {
        super(message);
    }

    public ProductNotInStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
