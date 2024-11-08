package com.join.inventory.exception;

import java.io.Serial;

public class DuplicateProductException extends ApiException {

    @Serial
    private static final long serialVersionUID = -1234567890123456789L;

    public DuplicateProductException(String productName) {
        super("DUPLICATE_PRODUCT", "A product with the name '" + productName + "' already exists.");
    }
}
