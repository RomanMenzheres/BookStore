package com.example.bookstore.service;

import com.example.bookstore.dto.cart.CartItemDto;
import com.example.bookstore.dto.cart.CreateCartItemRequestDto;
import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(User user);

    CartItemDto addCartItemToCart(CreateCartItemRequestDto requestDto, User user);

    CartItemDto updateQuantityOfCartItem(Long cartItemId, int quantity);

    void deleteCartItem(Long cartItemId);

    ShoppingCartDto registerShoppingCart(User user);
}
