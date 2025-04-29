package com.librairie.catalogue.validation;

import com.librairie.catalogue.repository.BookRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniqueISBNValidatorTest {

    private UniqueISBNValidator validator;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new UniqueISBNValidator(bookRepository);
    }

    @Test
    void shouldReturnTrueWhenIsbnIsNull() {
        boolean result = validator.isValid(null, context);
        assertThat(result).isFalse();
        verifyNoInteractions(bookRepository);
    }

    @Test
    void shouldReturnTrueWhenIsbnIsBlank() {
        boolean result = validator.isValid("  ", context);
        assertThat(result).isFalse();
        verifyNoInteractions(bookRepository);
    }

    @Test
    void shouldReturnFalseWhenIsbnAlreadyExists() {
        when(bookRepository.existsByIsbn("1234567890")).thenReturn(true);

        boolean result = validator.isValid("1234567890", context);

        assertThat(result).isFalse();
        verify(bookRepository).existsByIsbn("1234567890");
    }

    @Test
    void shouldReturnTrueWhenIsbnDoesNotExist() {
        when(bookRepository.existsByIsbn("0987654321")).thenReturn(false);

        boolean result = validator.isValid("0987654321", context);

        assertThat(result).isTrue();
        verify(bookRepository).existsByIsbn("0987654321");
    }

}