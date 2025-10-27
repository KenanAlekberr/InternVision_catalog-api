package com.example.catalogapi.dto.request.product;

import com.example.catalogapi.enums.AvailabilityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class UpdateProductRequest {
    @Size(min = 2, max = 150, message = "Product name length must be between 2 and 150")
    String name;

    @Size(min = 2, max = 500, message = "Description length must be between 2 and 500")
    String description;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal price;

    @Min(0)
    Integer productCount;

    AvailabilityStatus availability;

    Long categoryId;
}