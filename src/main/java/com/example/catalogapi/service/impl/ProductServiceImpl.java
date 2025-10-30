package com.example.catalogapi.service.impl;

import com.example.catalogapi.dto.request.product.CreateProductRequest;
import com.example.catalogapi.dto.request.product.UpdateProductRequest;
import com.example.catalogapi.dto.response.ProductResponse;
import com.example.catalogapi.entity.CategoryEntity;
import com.example.catalogapi.entity.ProductEntity;
import com.example.catalogapi.enums.AvailabilityStatus;
import com.example.catalogapi.exception.custom.NotFoundException;
import com.example.catalogapi.repository.CategoryRepository;
import com.example.catalogapi.repository.ProductRepository;
import com.example.catalogapi.repository.specification.ProductSpecification;
import com.example.catalogapi.service.abstraction.ProductService;
import com.example.catalogapi.util.CacheUtilWithRedisson;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.catalogapi.constant.AppConstants.ALL_PRODUCTS;
import static com.example.catalogapi.constant.AppConstants.PRODUCT_KEY_PREFIX;
import static com.example.catalogapi.constant.AppConstants.PRODUCT_LIST_KEY_PATTERN;
import static com.example.catalogapi.enums.AvailabilityStatus.IN_STOCK;
import static com.example.catalogapi.enums.AvailabilityStatus.LOW_STOCK;
import static com.example.catalogapi.enums.AvailabilityStatus.OUT_OF_STOCK;
import static com.example.catalogapi.exception.ExceptionConstants.CATEGORY_NOT_FOUND;
import static com.example.catalogapi.exception.ExceptionConstants.PRODUCT_NOT_FOUND;
import static com.example.catalogapi.mapper.ProductMapper.PRODUCT_MAPPER;
import static java.util.concurrent.TimeUnit.MINUTES;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    CacheUtilWithRedisson cache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse createProduct(CreateProductRequest request) {
        CategoryEntity category = fetchCategoryIfExist(request.getCategoryId());

        ProductEntity entity = PRODUCT_MAPPER.toEntity(request, category);
        productRepository.save(entity);

        ProductResponse response = PRODUCT_MAPPER.toResponse(entity);
        cache.set(PRODUCT_KEY_PREFIX + response.getId(), response, 5, MINUTES);
        cache.deleteByPattern(ALL_PRODUCTS);

        return response;
    }

    @Override
    public ProductResponse getProductById(Long id) {
        String key = PRODUCT_KEY_PREFIX + id;

        ProductResponse cached = cache.get(key, ProductResponse.class);

        if (cached != null)
            return cached;

        ProductEntity product = fetchProductIfExist(id);
        ProductResponse response = PRODUCT_MAPPER.toResponse(product);

        response.setAvailability(determineAvailability(product.getProductCount()));

        cache.set(key, response, 5, MINUTES);

        return response;
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size, Long categoryId, String name,
                                                BigDecimal minPrice, BigDecimal maxPrice) {
        String cacheKey = String.format(PRODUCT_LIST_KEY_PATTERN,
                page, size,
                (categoryId != null ? ":category=" + categoryId : "") +
                        (name != null ? ":name=" + name : "") +
                        (minPrice != null ? ":minPrice=" + minPrice : "") +
                        (maxPrice != null ? ":maxPrice=" + maxPrice : ""));

        List<ProductResponse> cachedList = cache.get(cacheKey, List.class);

        if (cachedList != null && !cachedList.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            return new PageImpl<>(cachedList, pageable, cachedList.size());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        var spec = ProductSpecification.filterBy(categoryId, name, minPrice, maxPrice);

        Page<ProductEntity> productPage = productRepository.findAll(spec, pageable);

        List<ProductResponse> responseList = productPage.getContent().stream()
                .map(product -> {
                    ProductResponse response = PRODUCT_MAPPER.toResponse(product);
                    response.setAvailability(determineAvailability(product.getProductCount()));
                    return response;
                }).toList();

        cache.set(cacheKey, responseList, 5, MINUTES);

        return new PageImpl<>(responseList, pageable, productPage.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        ProductEntity product = fetchProductIfExist(id);
        CategoryEntity category = null;

        if (request.getCategoryId() != null) category = fetchCategoryIfExist(request.getCategoryId());

        PRODUCT_MAPPER.updateEntity(product, request, category);
        productRepository.save(product);

        ProductResponse response = PRODUCT_MAPPER.toResponse(product);

        String key = PRODUCT_KEY_PREFIX + id;

        cache.set(key, response, 5, MINUTES);
        cache.deleteByPattern(ALL_PRODUCTS);

        return response;
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity product = fetchProductIfExist(id);

        productRepository.delete(product);

        cache.delete(PRODUCT_KEY_PREFIX + id);
        cache.deleteByPattern(ALL_PRODUCTS);
    }

    private ProductEntity fetchProductIfExist(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND.getCode(), PRODUCT_NOT_FOUND.getMessage()));
    }

    private CategoryEntity fetchCategoryIfExist(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND.getCode(), CATEGORY_NOT_FOUND.getMessage()));
    }

    private AvailabilityStatus determineAvailability(int productCount) {
        if (productCount <= 0) return OUT_OF_STOCK;
        else if (productCount < 5) return LOW_STOCK;
        else return IN_STOCK;
    }
}