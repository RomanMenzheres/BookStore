package com.example.bookstore.service.impl;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderItemDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.OrderCreationException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.OrderItemRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public List<OrderDto> findAllOrders(User user, Pageable pageable) {
        return orderRepository.findAllByUser(user, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto createOrder(CreateOrderRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(
                        () -> new OrderCreationException(
                                "Can't create order: shopping cart of user with id " + user.getId()
                                        + " is empty!"
                        )
                );

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderCreationException(
                    "Can't create order: shopping cart of user with id " + user.getId()
                            + " is empty!"
            );
        }

        Order order = new Order(user, Order.Status.PENDING,
                LocalDateTime.now(), requestDto.getShippingAddress());

        order.setOrderItems(createSetOfOrderItems(order, shoppingCart.getCartItems()));
        order.setTotal(getTotalPriceOfOrder(order.getOrderItems()));

        shoppingCartRepository.deleteById(shoppingCart.getId());

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public void updateOrderStatus(UpdateOrderRequestDto requestDto, Long orderId) {
        Order order = getOrderById(orderId);

        order.setStatus(Order.Status.valueOf(requestDto.getStatus()));
        orderRepository.save(order);
    }

    @Override
    public List<OrderItemDto> findOrderItems(Long orderId) {
        Order order = getOrderById(orderId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findOrderItemById(Long orderId, Long orderItemId) {
        Order order = getOrderById(orderId);
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order item by id: " + orderItemId)
        );

        if (!order.getOrderItems().contains(orderItem)) {
            throw new EntityNotFoundException("Can't find order item with id " + orderItemId
                    + " in order with id " + orderId);
        }

        return orderItemMapper.toDto(orderItem);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId)
                );
    }

    private Set<OrderItem> createSetOfOrderItems(Order order, Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(
                                order,
                                cartItem.getBook(),
                                cartItem.getQuantity(),
                                cartItem.getBook()
                                        .getPrice()
                                        .multiply(new BigDecimal(cartItem.getQuantity()))
                        )
                )
                .collect(Collectors.toSet());
    }

    private BigDecimal getTotalPriceOfOrder(Set<OrderItem> orderItems) {
        return new BigDecimal(
                orderItems.stream()
                        .map(OrderItem::getPrice)
                        .mapToInt(BigDecimal::intValue)
                        .sum()
        );
    }
}
