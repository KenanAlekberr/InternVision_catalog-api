package com.example.catalogapi.repository.specification;

import com.example.catalogapi.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<ProductEntity> filterBy(Long categoryId, String name,
                                                        BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (categoryId != null) predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));

            if (name != null && !name.isBlank())
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));

            if (minPrice != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));

            if (maxPrice != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));

            return predicate;
        };
    }
}