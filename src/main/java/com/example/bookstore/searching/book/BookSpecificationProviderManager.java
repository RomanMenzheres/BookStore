package com.example.bookstore.searching.book;

import com.example.bookstore.model.Book;
import com.example.bookstore.searching.SpecificationProvider;
import com.example.bookstore.searching.SpecificationProviderManager;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    public BookSpecificationProviderManager(
            List<SpecificationProvider<Book>> specificationProviders) {
        this.bookSpecificationProviders = specificationProviders;
    }

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(bookSpecificationProvider -> bookSpecificationProvider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Can't find correct specification for key: " + key));
    }
}
