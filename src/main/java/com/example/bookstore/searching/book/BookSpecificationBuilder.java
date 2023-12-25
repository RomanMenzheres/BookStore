package com.example.bookstore.searching.book;

import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.model.Book;
import com.example.bookstore.searching.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {
    private static final String AUTHOR_SEARCH_PARAMETER = "author";
    private static final String TITLE_SEARCH_PARAMETER = "title";
    private final BookSpecificationProviderManager specificationProviderManager;

    public BookSpecificationBuilder(BookSpecificationProviderManager specificationProviderManager) {
        this.specificationProviderManager = specificationProviderManager;
    }

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_SEARCH_PARAMETER)
                    .getSpecification(searchParameters.authors()));
        }

        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(TITLE_SEARCH_PARAMETER)
                    .getSpecification(searchParameters.titles()));
        }

        return spec;
    }
}
