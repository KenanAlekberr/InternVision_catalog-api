package com.example.catalogapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public enum ExceptionConstants {
    UNEXPECTED_EXCEPTION("UNEXPECTED_EXCEPTION", "Unexpected exception occurred"),
    HTTP_METHOD_IS_NOT_CORRECT("HTTP_METHOD_IS_NOT_CORRECT", "http method is not correct"),
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", "Category not found by id"),
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product not found by id"),
    ALREADY_EXCEPTION("ALREADY_EXCEPTION", "Category already exists"),
    VALIDATION_EXCEPTION("VALIDATION_EXCEPTION", "Validation exception");

    String code;
    String message;
}