package com.example.catalogapi.dto.request.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class CreateProductRequest {
    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 2, max = 150, message = "Product name length must be between 2 and 150")
    String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 2, max = 500, message = "Description length must be between 2 and 500")
    String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal price;

    @NotNull(message = "Inventory cannot be blank")
    @Min(0)
    Integer productCount;

    @NotNull(message = "Category id cannot be blank")
    Long categoryId;
}