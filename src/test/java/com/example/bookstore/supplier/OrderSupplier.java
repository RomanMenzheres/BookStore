package com.example.bookstore.supplier;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class OrderSupplier {

    public static Order getOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(UserSupplier.getUser());
        order.setOrderDate(LocalDateTime.of(2024, 1, 6, 16,36,36));
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress("124 Main St, City, Country");
        order.setOrderItems(Set.of(OrderItemSupplier.getOrderItem()));
        order.setTotal(BigDecimal.valueOf(19.99));
        return order;
    }

    public static OrderDto getOrderDto() {
        return new OrderDto(
                1L, 1L, Set.of(OrderItemSupplier.getOrderItemDto()),
                LocalDateTime.of(2024, 1, 6, 16,36,36),
                BigDecimal.valueOf(19.99), Order.Status.PENDING.name()
        );
    }

    public static CreateOrderRequestDto getCreateRequestOrderDto() {
        return new CreateOrderRequestDto()
                .setShippingAddress("123 Main St, City, Country");
    }

    public static CreateOrderRequestDto getInvalidCreateRequestOrderDto() {
        return new CreateOrderRequestDto()
                .setShippingAddress("");
    }

    public static UpdateOrderRequestDto getUpdateRequestDto() {
        return new UpdateOrderRequestDto()
                .setStatus(Order.Status.DELIVERED.name());
    }

    public static UpdateOrderRequestDto getInvalidUpdateOrderRequestDto() {
        return new UpdateOrderRequestDto()
                .setStatus("");
    }
}
