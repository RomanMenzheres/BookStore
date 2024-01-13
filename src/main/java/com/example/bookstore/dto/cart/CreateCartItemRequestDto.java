package com.example.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCartItemRequestDto {
    @NotNull
    @Min(0)
    private Long bookId;
    @Min(0)
    private int quantity;
}
