package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import com.example.bookstore.service.impl.ShoppingCartServiceImpl;
import com.example.bookstore.supplier.BookSupplier;
import com.example.bookstore.supplier.CartItemSupplier;
import com.example.bookstore.supplier.ShoppingCartSupplier;
import com.example.bookstore.supplier.UserSupplier;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Spy
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Verify getShoppingCart() method works when user has already had shopping cart")
    public void getShoppingCart_UserWithShoppingCart_Success() {
        User user = UserSupplier.getUser();
        ShoppingCart shoppingCart = ShoppingCartSupplier.getShoppingCart();
        ShoppingCartDto expected = ShoppingCartSupplier.getShoppingCartDto();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(user);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Verify getShoppingCart() method works when user has not had shopping cart yet")
    public void getShoppingCart_UserWithoutShoppingCart_Success() {
        User user = UserSupplier.getUser();
        ShoppingCart shoppingCart = ShoppingCartSupplier.getShoppingCart();
        ShoppingCartDto expected = ShoppingCartSupplier.getShoppingCartDto();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(user);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper, userRepository);
    }

    @Test
    @DisplayName("Verify getShoppingCart() method works when user is not registered")
    public void getShoppingCart_InvalidUser_Error() {
        User user = UserSupplier.getUser();

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCart(user)
        );

        String expected = "Can't find user with email: " + user.getEmail();
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verifyNoMoreInteractions(shoppingCartRepository, userRepository);
    }

    @Test
    @DisplayName("""
            Verify addCartItemToCart() method works
             with valid CreateCartItemRequestDto and user
            """)
    public void addCartItemToCart_ValidRequestDtoAndUser_Success() {
        CreateCartItemRequestDto requestDto = CartItemSupplier.getCreateCartItemRequestDto();
        User user = UserSupplier.getUser();
        ShoppingCart shoppingCart = ShoppingCartSupplier.getShoppingCart();
        Book book = BookSupplier.getBook();
        ShoppingCartDto shoppingCartDto = ShoppingCartSupplier.getShoppingCartDto();
        CartItem cartItem = CartItemSupplier.getCartItem();
        CartItemDto expected = CartItemSupplier.getCartItemDto();

        doReturn(shoppingCartDto).when(shoppingCartService).getShoppingCart(user);

        when(bookRepository.findById(requestDto.getBookId())).thenReturn(Optional.of(book));
        when(shoppingCartMapper.toModel(shoppingCartDto)).thenReturn(shoppingCart);
        when(cartItemRepository.findByBookIdAndShoppingCartId(book.getId(), shoppingCart.getId()))
                .thenReturn(Optional.empty());
        when(cartItemMapper.toModel(requestDto)).thenReturn(cartItem);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(expected);

        CartItemDto actual = shoppingCartService.addCartItemToCart(requestDto, user);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(requestDto.getBookId());
        verify(shoppingCartMapper, times(1)).toModel(shoppingCartDto);
        verify(cartItemRepository, times(1))
                .findByBookIdAndShoppingCartId(book.getId(), shoppingCart.getId());
        verify(cartItemMapper, times(1)).toModel(requestDto);
        verify(cartItemRepository, times(1)).save(cartItem);
        verify(cartItemMapper, times(1)).toDto(cartItem);
        verifyNoMoreInteractions(
                bookRepository, shoppingCartMapper, cartItemRepository, cartItemMapper
        );
    }

    @Test
    @DisplayName("""
            Verify addCartItemToCart() method works
             when book not exist
            """)
    public void addCartItemToCart_BookNotExist_Success() {
        CreateCartItemRequestDto requestDto = CartItemSupplier.getInvalidCreateCartItemRequestDto();

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.addCartItemToCart(requestDto, new User())
        );

        String expected = "Can't find book by id: " + requestDto.getBookId();
        String actual = exception.getMessage();

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(requestDto.getBookId());
        verifyNoMoreInteractions(
                bookRepository, shoppingCartMapper, cartItemRepository, cartItemMapper
        );
    }

    @Test
    @DisplayName("""
            Verify addCartItemToCart() method works
             when cart item has already created
            """)
    public void addCartItemToCart_CartItemAlreadyExist_Success() {
        CreateCartItemRequestDto requestDto = CartItemSupplier.getCreateCartItemRequestDto();
        User user = UserSupplier.getUser();
        ShoppingCart shoppingCart = ShoppingCartSupplier.getShoppingCart();
        Book book = BookSupplier.getBook();
        ShoppingCartDto shoppingCartDto = ShoppingCartSupplier.getShoppingCartDto();
        CartItem cartItem = CartItemSupplier.getCartItem();
        CartItemDto expected = CartItemSupplier.getCartItemDtoWith2Quantity();

        doReturn(shoppingCartDto).when(shoppingCartService).getShoppingCart(user);
        doReturn(expected).when(shoppingCartService)
                .updateQuantityOfCartItem(expected.id(), expected.quantity());

        when(bookRepository.findById(requestDto.getBookId())).thenReturn(Optional.of(book));
        when(shoppingCartMapper.toModel(shoppingCartDto)).thenReturn(shoppingCart);
        when(cartItemRepository.findByBookIdAndShoppingCartId(book.getId(), shoppingCart.getId()))
                .thenReturn(Optional.of(cartItem));

        CartItemDto actual = shoppingCartService.addCartItemToCart(requestDto, user);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(requestDto.getBookId());
        verify(shoppingCartMapper, times(1)).toModel(shoppingCartDto);
        verify(cartItemRepository, times(1))
                .findByBookIdAndShoppingCartId(book.getId(), shoppingCart.getId());
        verifyNoMoreInteractions(
                bookRepository, shoppingCartMapper, cartItemRepository, cartItemMapper
        );
    }
}
