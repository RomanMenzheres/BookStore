package com.example.bookstore.supplier;

import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.model.OrderItem;

import java.math.BigDecimal;

public class OrderItemSupplier {

    public static OrderItem getOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setBook(BookSupplier.getBook());
        orderItem.setPrice(BigDecimal.valueOf(19.99));
        orderItem.setQuantity(1);
        return orderItem;
    }

    public static OrderItemDto getOrderItemDto() {
        return new OrderItemDto(1L, 1L, 1);
    }
}
