package com.join.inventory.exception;

import java.io.Serial;

public class CategoryNotFoundException extends ApiException {
	
	@Serial
    private static final long serialVersionUID = -3867403756842065065L;

	public CategoryNotFoundException(Long categoryId) {
        super("CATEGORY_NOT_FOUND", categoryId);
    }
}