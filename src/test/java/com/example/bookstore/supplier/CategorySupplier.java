package com.example.bookstore.supplier;

import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.model.Category;
import java.util.List;

public class CategorySupplier {
    public static Category getCategory() {
        Category category = new Category();
        category.setName("Category");
        category.setDescription("Unreal category");
        return category;
    }

    public static CategoryDto getCategoryDto() {
        return new CategoryDto(
                1L, "Category", "Unreal category"
        );
    }

    public static CreateCategoryRequestDto getCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("Category")
                .setDescription("Unreal category");
    }

    public static CreateCategoryRequestDto getInvalidCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("")
                .setDescription("");
    }

    public static List<CategoryDto> getAllCategoriesInDb() {
        return List.of(
                getCategoryWithId1(),
                new CategoryDto(2L, "Historical fiction", "Historical books"),
                new CategoryDto(3L, "Mystery", "Mystery books")
        );
    }

    public static CategoryDto getCategoryWithId1() {
        return new CategoryDto(1L, "Fantasy", "Fantasy books");
    }
}
