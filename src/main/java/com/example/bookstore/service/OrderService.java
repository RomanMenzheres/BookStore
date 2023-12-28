package com.example.bookstore.service;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    List<OrderDto> findAllOrders(User user, Pageable pageable);

    OrderDto createOrder(CreateOrderRequestDto requestDto, User user);

    void updateOrderStatus(UpdateOrderRequestDto requestDto, Long orderId);

    List<OrderItemDto> findOrderItems(Long orderId);

    OrderItemDto findOrderItemById(Long orderId, Long orderItemId);
}
