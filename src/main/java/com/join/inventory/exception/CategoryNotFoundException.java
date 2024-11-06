package com.join.inventory.exception;

public class CategoryNotFoundException extends ApiException {
	
	private static final long serialVersionUID = -3867403756842065065L;

	public CategoryNotFoundException(Long categoryId) {
        super("CATEGORY_NOT_FOUND", categoryId);
    }
}