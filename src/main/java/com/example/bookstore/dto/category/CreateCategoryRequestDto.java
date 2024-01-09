package com.example.bookstore.dto.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class CreateCategoryRequestDto {
    @NotEmpty
    @Length(max = 255)
    private String name;
    @NotNull
    @Length(max = 255)
    private String description;
}
