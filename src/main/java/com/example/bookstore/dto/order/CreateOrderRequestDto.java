package com.example.bookstore.dto.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateOrderRequestDto {
    @NotEmpty
    private String shippingAddress;
}
