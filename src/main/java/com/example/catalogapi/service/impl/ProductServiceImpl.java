package com.example.catalogapi.service.impl;

import com.example.catalogapi.dto.request.product.CreateProductRequest;
import com.example.catalogapi.dto.request.product.UpdateProductRequest;
import com.example.catalogapi.dto.response.ProductResponse;
import com.example.catalogapi.entity.CategoryEntity;
import com.example.catalogapi.entity.ProductEntity;
import com.example.catalogapi.exception.custom.NotFoundException;
import com.example.catalogapi.repository.CategoryRepository;
import com.example.catalogapi.repository.ProductRepository;
import com.example.catalogapi.service.abstraction.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.catalogapi.exception.ExceptionConstants.CATEGORY_NOT_FOUND;
import static com.example.catalogapi.exception.ExceptionConstants.PRODUCT_NOT_FOUND;
import static com.example.catalogapi.mapper.ProductMapper.PRODUCT_MAPPER;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse createProduct(CreateProductRequest request) {
        CategoryEntity category = fetchCategoryIfExist(request.getCategoryId());

        ProductEntity entity = PRODUCT_MAPPER.toEntity(request, category);
        productRepository.save(entity);

        return PRODUCT_MAPPER.toResponse(entity);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        ProductEntity product = fetchProductIfExist(id);

        return PRODUCT_MAPPER.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<ProductEntity> productPage;

        if (categoryId != null) productPage = productRepository.findByCategoryId(categoryId, pageable);
        else productPage = productRepository.findAll(pageable);

        return productPage.map(PRODUCT_MAPPER::toResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        ProductEntity product = fetchProductIfExist(id);

        CategoryEntity category = null;

        if (request.getCategoryId() != null)
            category = fetchCategoryIfExist(request.getCategoryId());

        PRODUCT_MAPPER.updateEntity(product, request, category);
        productRepository.save(product);

        return PRODUCT_MAPPER.toResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity product = fetchProductIfExist(id);

        productRepository.delete(product);
    }

    private ProductEntity fetchProductIfExist(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND.getCode(), PRODUCT_NOT_FOUND.getMessage()));
    }

    private CategoryEntity fetchCategoryIfExist(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND.getCode(), CATEGORY_NOT_FOUND.getMessage()));
    }
}