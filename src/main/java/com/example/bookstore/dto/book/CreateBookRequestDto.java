package com.example.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
