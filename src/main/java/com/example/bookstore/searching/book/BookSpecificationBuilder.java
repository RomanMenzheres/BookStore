package com.example.bookstore.searching.book;

import com.example.bookstore.dto.BookSearchParameters;
import com.example.bookstore.model.Book;
import com.example.bookstore.searching.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {

    private final BookSpecificationProviderManager specificationProviderManager;

    public BookSpecificationBuilder(BookSpecificationProviderManager specificationProviderManager) {
        this.specificationProviderManager = specificationProviderManager;
    }

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParameters.authors()));
        }

        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParameters.titles()));
        }

        return spec;
    }
}
