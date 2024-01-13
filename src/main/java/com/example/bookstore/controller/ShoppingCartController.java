package com.example.bookstore.controller;

import com.example.bookstore.dto.cart.CartItemDto;
import com.example.bookstore.dto.cart.CreateCartItemRequestDto;
import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.dto.cart.UpdateCartItemRequestDto;
import com.example.bookstore.model.User;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart", description = "Endpoints for managing user's shopping cart")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart",
            description = "Get all cart items of user's shopping cart")
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        return shoppingCartService.getShoppingCart((User) authentication.getPrincipal());
    }

    @Operation(summary = "Add cart item to shopping cart",
            description = "Add new cart item to user's shopping cart")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemDto addCartItem(Authentication authentication,
                                   @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.addCartItemToCart(
                requestDto, (User) authentication.getPrincipal()
        );
    }

    @Operation(summary = "Update cart item", description = "Update quantity of cart item")
    @PutMapping("/cart-items/{cartItemId}")
    public CartItemDto updateQuantityOfCartItem(
            @RequestBody UpdateCartItemRequestDto requestDto,
            @PathVariable("cartItemId") Long cartItemId
    ) {
        return shoppingCartService.updateQuantityOfCartItem(cartItemId, requestDto.quantity());
    }

    @Operation(summary = "Delete cart item", description = "Delete cart item from shopping cart")
    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable("cartItemId") Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
