package com.example.bookstore.supplier;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.model.Book;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class BookSupplier {
    public static BookDto getBookDto() {
        BookDto expected = new BookDto();
        expected.setTitle("Title");
        expected.setAuthor("Author");
        expected.setIsbn("978-3-16-148410-0");
        expected.setPrice(BigDecimal.valueOf(19.99));
        expected.setDescription("description");
        expected.setCoverImage("https://example.com/updated-cover-image.jpg");
        return expected;
    }

    public static Book getBook() {
        Book book = new Book();
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setIsbn("978-3-16-148410-0");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("description");
        book.setCoverImage("https://example.com/updated-cover-image.jpg");
        return book;
    }

    public static CreateBookRequestDto getCreateBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Title")
                .setAuthor("Author")
                .setIsbn("978-3-16-148410-0")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
                .setCategoryIds(Set.of(1L, 3L));
    }

    public static CreateBookRequestDto getInvalidCreateBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("")
                .setAuthor("")
                .setIsbn("978-3-16-148410-0")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("")
                .setCoverImage("");
    }

    public static List<BookDto> getAllBooksInDb() {
        return List.of(
                getBookWithId1(),

                new BookDto()
                        .setId(2L)
                        .setTitle("History Year by Year")
                        .setAuthor("Dorling Kindersley")
                        .setIsbn("978-3-16-148412-0")
                        .setPrice(BigDecimal.valueOf(19.99))
                        .setDescription("Historical book")
                        .setCoverImage("https://example.com/updated-cover-image.jpg"),

                new BookDto()
                        .setId(3L)
                        .setTitle("The Silent Patient")
                        .setAuthor("Alex Michaelides")
                        .setIsbn("978-3-16-148413-0")
                        .setPrice(BigDecimal.valueOf(21.99))
                        .setDescription("Mystery book")
                        .setCoverImage("https://example.com/updated-cover-image.jpg")
        );
    }

    public static BookDto getBookWithId1() {
        return new BookDto()
                .setId(1L)
                .setTitle("Harry Potter and the Prisoner of Azkaban")
                .setAuthor("J.K. Rowling")
                .setIsbn("978-3-16-148411-0")
                .setPrice(BigDecimal.valueOf(29.99))
                .setDescription("Fantasy book")
                .setCoverImage("https://example.com/updated-cover-image.jpg");
    }
}
