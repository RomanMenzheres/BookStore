package com.example.bookstore;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Harry Potter come back is real!");
            book.setAuthor("Mad Woman");
            book.setPrice(BigDecimal.valueOf(999));
            book.setIsbn("1");

            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
