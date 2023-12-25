package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list of all available books")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new book", description = "Create a new book")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the book by id", description = "Get the book by id")
    public BookDto getById(@PathVariable("id") Long id) {
        return bookService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update the book by id",
            description = "Update the information about book by id")
    public BookDto updateById(@PathVariable("id") Long id,
                              @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the book by id", description = "Delete the book by id")
    public BookDto deleteById(@PathVariable("id") Long id) {
        return bookService.delete(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for books", description = "Searching books by some parameters")
    public List<BookDto> search(BookSearchParameters searchParameters) {
        return bookService.search(searchParameters);
    }
}
