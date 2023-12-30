package com.example.bookstore.controller;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.model.User;
import com.example.bookstore.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getOrders(Authentication authentication, Pageable pageable) {
        return orderService.findAllOrders((User) authentication.getPrincipal(), pageable);
    }

    @PostMapping
    public OrderDto createOrder(Authentication authentication,
                                @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.createOrder(requestDto, (User) authentication.getPrincipal());
    }

    @PatchMapping("/{orderId}")
    public void updateStatusOfOrder(@PathVariable("orderId") Long orderId,
                                    @RequestBody @Valid UpdateOrderRequestDto requestDto) {
        orderService.updateOrderStatus(requestDto, orderId);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getItemsOfOrder(@PathVariable("orderId") Long orderId) {
        return orderService.findOrderItems(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getItemOfOrderById(@PathVariable("orderId") Long orderId,
                                           @PathVariable("itemId") Long itemId) {
        return orderService.findOrderItemById(orderId, itemId);
    }
}
