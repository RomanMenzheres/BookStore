package com.example.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
            Verify save() method works
            """)
    public void save_ValidCreateRequestDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Title")
                .setAuthor("Author")
                .setIsbn("0-061-96436-0")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg");

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle(book.getTitle());
        expected.setAuthor(book.getAuthor());
        expected.setIsbn(book.getIsbn());
        expected.setPrice(book.getPrice());
        expected.setDescription(book.getDescription());
        expected.setCoverImage(book.getCoverImage());

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.save(requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).save(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Verify findAll() method works
            """)
    public void findAll_ValidPageable_ReturnsAllBooks() {
        Book book = new Book();
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setIsbn("0-061-96436-0");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("description");
        book.setCoverImage("https://example.com/updated-cover-image.jpg");

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> actual = bookService.findAll(pageable);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(bookDto);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Verify the correct book was returned when book exists
            """)
    public void findById_ValidId_ReturnsBookDto() {
        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setIsbn("0-061-96436-0");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("description");
        book.setCoverImage("https://example.com/updated-cover-image.jpg");

        BookDto expected = new BookDto();
        expected.setId(bookId);
        expected.setTitle(book.getTitle());
        expected.setAuthor(book.getAuthor());
        expected.setIsbn(book.getIsbn());
        expected.setPrice(book.getPrice());
        expected.setDescription(book.getDescription());
        expected.setCoverImage(book.getCoverImage());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.findById(bookId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Verify the exception was thrown when invalid id
            """)
    public void findById_InvalidId_ThrowsException() {
        Long bookId = null;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );

        String expected = "Can't find book with id: " + bookId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
