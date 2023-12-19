package com.example.bookstore.searching;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T, K> {
    Specification<T> build(K searchParameters);
}
