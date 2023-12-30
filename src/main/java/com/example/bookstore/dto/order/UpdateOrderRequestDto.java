package com.example.bookstore.dto.order;

import com.example.bookstore.model.Order;
import com.example.bookstore.validation.ValueOfEnum;
import lombok.Data;

@Data
public class UpdateOrderRequestDto {
    @ValueOfEnum(enumClass = Order.Status.class)
    private String status;
}
