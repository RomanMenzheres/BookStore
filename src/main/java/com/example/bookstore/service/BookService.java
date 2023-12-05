package com.example.bookstore.service;

import java.util.List;
import com.example.bookstore.entity.Book;

public interface BookService {
    Book save(Book book);
    List<Book> findAll();
}
