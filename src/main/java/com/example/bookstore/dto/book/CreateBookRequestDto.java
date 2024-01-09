package com.example.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotEmpty
    @Length(max = 255)
    private String title;
    @NotEmpty
    @Length(max = 255)
    private String author;
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Length(max = 255)
    private String description;
    @NotNull
    @Length(max = 255)
    private String coverImage;
    @NotEmpty
    private Set<Long> categoryIds = new HashSet<>();
}
