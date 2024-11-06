package com.join.inventory.exception;

public class DuplicateProductException extends ApiException {
    
    private static final long serialVersionUID = -1234567890123456789L;

    public DuplicateProductException(String productName) {
        super("DUPLICATE_PRODUCT", "A product with the name '" + productName + "' already exists.");
    }
}
