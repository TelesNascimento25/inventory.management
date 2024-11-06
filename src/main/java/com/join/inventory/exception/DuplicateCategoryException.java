package com.join.inventory.exception;

public class DuplicateCategoryException extends ApiException {
    
    private static final long serialVersionUID = -1234567890123456789L;

    public DuplicateCategoryException(String categoryName) {
        super("DUPLICATE_CATEGORY", "A category with the name '" + categoryName + "' already exists.");
    }
}
