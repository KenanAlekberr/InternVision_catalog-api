package com.example.catalogapi.service.impl;

import com.example.catalogapi.dto.request.category.CreateCategoryRequest;
import com.example.catalogapi.dto.request.category.UpdateCategoryRequest;
import com.example.catalogapi.dto.response.CategoryResponse;
import com.example.catalogapi.entity.CategoryEntity;
import com.example.catalogapi.exception.custom.AlreadyExistException;
import com.example.catalogapi.exception.custom.NotFoundException;
import com.example.catalogapi.repository.CategoryRepository;
import com.example.catalogapi.service.abstraction.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.catalogapi.exception.ExceptionConstants.ALREADY_EXCEPTION;
import static com.example.catalogapi.exception.ExceptionConstants.CATEGORY_NOT_FOUND;
import static com.example.catalogapi.mapper.CategoryMapper.CATEGORY_MAPPER;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName()))
            throw new AlreadyExistException(ALREADY_EXCEPTION.getCode(), ALREADY_EXCEPTION.getMessage());

        CategoryEntity entity = CATEGORY_MAPPER.toEntity(request);
        categoryRepository.save(entity);

        return CATEGORY_MAPPER.toResponse(entity);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        CategoryEntity category = fetchCategoryIfExist(id);

        return CATEGORY_MAPPER.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CATEGORY_MAPPER::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        CategoryEntity entity = fetchCategoryIfExist(id);

        CATEGORY_MAPPER.updateEntity(entity, request);
        categoryRepository.save(entity);

        return CATEGORY_MAPPER.toResponse(entity);
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryEntity category = fetchCategoryIfExist(id);

        categoryRepository.delete(category);
    }

    private CategoryEntity fetchCategoryIfExist(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND.getCode(), CATEGORY_NOT_FOUND.getMessage()));
    }
}