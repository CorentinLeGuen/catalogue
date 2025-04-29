package com.librairie.catalogue.validation;


import com.librairie.catalogue.repository.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueISBNValidator implements ConstraintValidator<UniqueISBN, String> {
    private final BookRepository bookRepository;

    public UniqueISBNValidator(BookRepository repository) {
        this.bookRepository = repository;
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null || isbn.isBlank()) {
            return false;
        }
        return !bookRepository.existsByIsbn(isbn);
    }
}
