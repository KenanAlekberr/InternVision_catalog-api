package com.example.catalogapi.mapper;

import com.example.catalogapi.dto.request.category.CreateCategoryRequest;
import com.example.catalogapi.dto.request.category.UpdateCategoryRequest;
import com.example.catalogapi.dto.response.CategoryResponse;
import com.example.catalogapi.entity.CategoryEntity;
import io.micrometer.common.util.StringUtils;

public enum CategoryMapper {
    CATEGORY_MAPPER;

    public CategoryEntity toEntity(CreateCategoryRequest request) {
        return CategoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public CategoryResponse toResponse(CategoryEntity entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(CategoryEntity entity, UpdateCategoryRequest request) {
        if (StringUtils.isNotEmpty(request.getName()))
            entity.setName(request.getName());

        if (StringUtils.isNotEmpty(request.getDescription()))
            entity.setDescription(request.getDescription());
    }
}