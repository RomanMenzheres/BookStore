package com.example.bookstore.repository;

import java.util.List;
import com.example.bookstore.entity.Book;

public interface BookRepository {
    Book save(Book book);
    List<Book> findAll();
}
