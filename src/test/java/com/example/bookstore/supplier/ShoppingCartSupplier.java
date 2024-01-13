package com.example.bookstore.supplier;

import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.model.ShoppingCart;
import java.util.Set;

public class ShoppingCartSupplier {

    public static ShoppingCartDto getShoppingCartDto() {
        return new ShoppingCartDto(1L, 1L, Set.of());
    }

    public static ShoppingCart getShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(UserSupplier.getUser());
        return shoppingCart;
    }
}
