package com.example.bookstore.service.impl;

import com.example.bookstore.dto.cart.CartItemDto;
import com.example.bookstore.dto.cart.CreateCartItemRequestDto;
import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.ShoppingCartService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getShoppingCart(User user) {
        Optional<ShoppingCart> byUser = shoppingCartRepository.findByUser(user);
        if (byUser.isPresent()) {
            return shoppingCartMapper.toDto(byUser.get());
        }

        return registerShoppingCart(user);
    }

    @Override
    public CartItemDto addCartItemToCart(CreateCartItemRequestDto requestDto, User user) {
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find book by id: " + requestDto.getBookId()
                        )
                );

        ShoppingCart shoppingCart = shoppingCartMapper.toModel(getShoppingCart(user));

        Optional<CartItem> byBookIdAndShoppingCartId =
                cartItemRepository.findByBookIdAndShoppingCartId(
                        book.getId(), shoppingCart.getId()
                );
        if (byBookIdAndShoppingCartId.isPresent()) {
            CartItem cartItem = byBookIdAndShoppingCartId.get();
            return updateQuantityOfCartItem(
                    cartItem.getId(), cartItem.getQuantity() + requestDto.getQuantity()
            );
        }

        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(book);
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        shoppingCart.getCartItems().add(savedCartItem);
        shoppingCartRepository.save(shoppingCart);

        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public CartItemDto updateQuantityOfCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find cart item by id: " + cartItemId
                        )
                );

        cartItem.setQuantity(quantity);

        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public ShoppingCartDto registerShoppingCart(User user) {
        User byEmail = userRepository.findByEmail(user.getEmail())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find user with email: " + user.getEmail()
                        )
                );
        return shoppingCartMapper.toDto(
                shoppingCartRepository.save(new ShoppingCart(byEmail))
        );
    }
}
