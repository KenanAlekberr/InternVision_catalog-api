package com.example.catalogapi.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class CreateCategoryRequest {
    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 2, max = 100, message = "Category name length must be between 2 and 100")
    String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 2, max = 255, message = "Description length must be between 2 and 255")
    String description;
}