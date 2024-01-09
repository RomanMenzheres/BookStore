package com.example.bookstore.supplier;

import com.example.bookstore.dto.cart.CartItemDto;
import com.example.bookstore.dto.cart.CreateCartItemRequestDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;

public class CartItemSupplier {

    public static CreateCartItemRequestDto getCreateCartItemRequestDto() {
        return new CreateCartItemRequestDto()
                .setBookId(1L)
                .setQuantity(1);
    }

    public static CreateCartItemRequestDto getInvalidCreateCartItemRequestDto() {
        return new CreateCartItemRequestDto()
                .setBookId(null)
                .setQuantity(1);
    }

    public static CartItem getCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(BookSupplier.getBook());
        cartItem.setShoppingCart(ShoppingCartSupplier.getShoppingCart());
        cartItem.setQuantity(1);
        return cartItem;
    }

    public static CartItemDto getCartItemDto() {
        Book book = BookSupplier.getBook();
        return new CartItemDto(1L, book.getId(), book.getTitle(), 1);
    }

    public static CartItemDto getCartItemDtoWith2Quantity() {
        Book book = BookSupplier.getBook();
        return new CartItemDto(1L, book.getId(), book.getTitle(), 2);
    }
}
