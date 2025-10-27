package com.example.catalogapi.dto.request.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UpdateCategoryRequest {
    @Size(min = 2, max = 100, message = "Category name length must be between 2 and 100")
    String name;

    @Size(min = 2, max = 255, message = "Description length must be between 2 and 255")
    String description;
}