package com.join.inventory.exception;

public class ProductNotFoundException extends ApiException {
	
	private static final long serialVersionUID = -3867403756842065065L;

	public ProductNotFoundException(Long productId) {
        super("PRODUCT_NOT_FOUND", productId);
    }
}