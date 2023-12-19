package com.example.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    private static final String PATTERN_OF_ISBN_CODE =
            "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$";

    @Override
    public boolean isValid(String isbnCode, ConstraintValidatorContext constraintValidatorContext) {
        return isbnCode != null
                && Pattern.compile(PATTERN_OF_ISBN_CODE).matcher(isbnCode).matches();
    }
}
