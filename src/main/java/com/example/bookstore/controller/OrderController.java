package com.example.bookstore.controller;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.model.User;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User's Orders", description = "Endpoints for creating and checking orders")
@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get All Orders",
            description = "Get all user's orders")
    @GetMapping
    public List<OrderDto> getOrders(Authentication authentication, Pageable pageable) {
        return orderService.findAllOrders((User) authentication.getPrincipal(), pageable);
    }

    @Operation(summary = "Create order",
            description = "Creating order with all in your shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderDto createOrder(Authentication authentication,
                                @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.createOrder(requestDto, (User) authentication.getPrincipal());
    }

    @Operation(summary = "Update Order's Status",
            description = "Change order's status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{orderId}")
    public void updateStatusOfOrder(@PathVariable("orderId") Long orderId,
                                    @RequestBody @Valid UpdateOrderRequestDto requestDto) {
        orderService.updateOrderStatus(requestDto, orderId);
    }

    @Operation(summary = "Get Items Of Specific Order",
            description = "Get All Items of Order by id")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getItemsOfOrder(@PathVariable("orderId") Long orderId) {
        return orderService.findOrderItems(orderId);
    }

    @Operation(summary = "Get Specific Item Of Specific Order",
            description = "Get item by id of order by id")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getItemOfOrderById(@PathVariable("orderId") Long orderId,
                                           @PathVariable("itemId") Long itemId) {
        return orderService.findOrderItemById(orderId, itemId);
    }
}
