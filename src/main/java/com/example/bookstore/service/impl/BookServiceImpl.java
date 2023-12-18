package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParameters;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.searching.book.BookSpecificationBuilder;
import com.example.bookstore.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           BookMapper bookMapper,
                           BookSpecificationBuilder bookSpecificationBuilder) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookSpecificationBuilder = bookSpecificationBuilder;
    }

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(requestDto)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(
                bookRepository.findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Can't find book with id: " + id)
                        )
        );
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        findById(id);
        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto delete(Long id) {
        return bookMapper.toDto(bookRepository.deleteBookById(id));
    }

    @Override
    public List<BookDto> search(BookSearchParameters searchParameters) {
        return bookRepository.findAll(bookSpecificationBuilder.build(searchParameters)).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
