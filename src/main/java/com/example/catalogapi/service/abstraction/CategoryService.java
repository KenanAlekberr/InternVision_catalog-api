package com.example.catalogapi.service.abstraction;

import com.example.catalogapi.dto.request.category.CreateCategoryRequest;
import com.example.catalogapi.dto.request.category.UpdateCategoryRequest;
import com.example.catalogapi.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    CategoryResponse updateCategory(Long id, UpdateCategoryRequest request);

    void deleteCategory(Long id);
}