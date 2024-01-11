package com.example.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.OrderCreationException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.OrderItemRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.impl.OrderServiceImpl;
import com.example.bookstore.supplier.CartItemSupplier;
import com.example.bookstore.supplier.OrderSupplier;
import com.example.bookstore.supplier.ShoppingCartSupplier;
import com.example.bookstore.supplier.UserSupplier;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Spy
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Verify findAll() method works with valid user")
    public void findAll_UserWithOrders_Success() {
        User user = UserSupplier.getUser();
        Order order = OrderSupplier.getOrder();
        OrderDto orderDto = OrderSupplier.getOrderDto();

        List<Order> expected = List.of(order);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(expected, pageable, expected.size());

        when(orderRepository.findAllByUser(user, pageable)).thenReturn(orderPage);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> actual = orderService.findAllOrders(user, pageable);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(orderDto);
        verify(orderRepository, times(1)).findAllByUser(user, pageable);
        verify(orderMapper, times(1)).toDto(order);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    @DisplayName("Verify createOrder() method works with valid input data")
    public void createOrder_ValidData_Success() {
        User user = UserSupplier.getUser();
        CartItem cartItem = CartItemSupplier.getCartItem();
        ShoppingCart shoppingCart = ShoppingCartSupplier.getShoppingCart();
        shoppingCart.setCartItems(Set.of(cartItem));
        Order order = OrderSupplier.getOrder();
        OrderDto expected = OrderSupplier.getOrderDto();
        CreateOrderRequestDto requestDto = OrderSupplier.getCreateRequestOrderDto();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expected);

        OrderDto actual = orderService.createOrder(requestDto, user);

        assertEquals(expected, actual);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository, times(1)).deleteById(anyLong());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toDto(order);
        verifyNoMoreInteractions(shoppingCartRepository, orderRepository, orderMapper);
    }

    @Test
    @DisplayName("Verify createOrder() method works when user does not have shopping cart")
    public void createOrder_UserWithoutShoppingCart_Exception() {
        User user = UserSupplier.getUser();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.empty());

        OrderCreationException exception = assertThrows(
                OrderCreationException.class,
                () -> orderService.createOrder(new CreateOrderRequestDto(), user)
        );

        String expected = "Can't create order: shopping cart of user with id " + user.getId()
                + " is empty!";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verifyNoMoreInteractions(shoppingCartRepository, orderRepository, orderMapper);
    }

    @Test
    @DisplayName("Verify createOrder() method works when user's shopping cart is empty")
    public void createOrder_ShoppingCartIsEmpty_Exception() {
        User user = UserSupplier.getUser();
        ShoppingCart shoppingCart = ShoppingCartSupplier.getShoppingCart();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));

        OrderCreationException exception = assertThrows(
                OrderCreationException.class,
                () -> orderService.createOrder(new CreateOrderRequestDto(), user)
        );

        String expected = "Can't create order: shopping cart of user with id " + user.getId()
                + " is empty!";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verifyNoMoreInteractions(shoppingCartRepository, orderRepository, orderMapper);
    }

    @Test
    @DisplayName("Verify updateOrderStatus() method works")
    public void updateOrderStatus_ValidData_Success() {
        Order order = OrderSupplier.getOrder();
        UpdateOrderRequestDto requestDto = OrderSupplier.getUpdateRequestDto();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(requestDto, order.getId());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Verify updateOrderStatus() method works")
    public void updateOrderStatus_OrderNotExists_Success() {
        Long orderId = 1L;
        UpdateOrderRequestDto requestDto = OrderSupplier.getUpdateRequestDto();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> orderService.updateOrderStatus(requestDto, orderId)
        );

        String expected = "Can't find order by id: " + orderId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(orderRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(orderRepository);
    }
}
