package com.example.catalogapi.mapper;

import com.example.catalogapi.dto.request.product.CreateProductRequest;
import com.example.catalogapi.dto.request.product.UpdateProductRequest;
import com.example.catalogapi.dto.response.ProductResponse;
import com.example.catalogapi.entity.CategoryEntity;
import com.example.catalogapi.entity.ProductEntity;
import io.micrometer.common.util.StringUtils;

import static com.example.catalogapi.enums.AvailabilityStatus.IN_STOCK;

public enum ProductMapper {
    PRODUCT_MAPPER;

    public ProductEntity toEntity(CreateProductRequest request, CategoryEntity category) {
        return ProductEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .productCount(request.getProductCount())
                .availability(IN_STOCK)
                .category(category)
                .build();
    }

    public ProductResponse toResponse(ProductEntity entity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .productCount(entity.getProductCount())
                .availability(entity.getAvailability())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(ProductEntity entity, UpdateProductRequest request, CategoryEntity category) {
        if (StringUtils.isNotEmpty(request.getName()))
            entity.setName(request.getName());

        if (StringUtils.isNotEmpty(request.getDescription()))
            entity.setDescription(request.getDescription());

        if (request.getPrice() != null)
            entity.setPrice(request.getPrice());

        if (request.getProductCount() != null)
            entity.setProductCount(request.getProductCount());

        if (request.getAvailability() != null)
            entity.setAvailability(request.getAvailability());

        if (category != null)
            entity.setCategory(category);
    }
}