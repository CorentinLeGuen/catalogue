package com.librairie.catalogue.service;

import com.librairie.catalogue.exception.ResourceNotFoundException;
import com.librairie.catalogue.mapper.BookMapper;
import com.librairie.catalogue.model.Author;
import com.librairie.catalogue.model.Book;
import com.librairie.catalogue.model.dto.BookDTO;
import com.librairie.catalogue.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Mock
    private AuthorService authorService;

    private Book book;
    private BookDTO bookDTO;
    private BookDTO bookDTOWithEmptyAuthorList;
    private final Author author = Author.builder().name("test 1").build();

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .isbn("9999999999999")
                .title("Le Petit Prince")
                .summary("Un livre sur l'aviation, sur les moutons et les princes.")
                .pageCount(100)
                .build();

        List<String> authorList = new ArrayList<>();
        authorList.add(author.getName());

        bookDTO = BookDTO.builder()
                .isbn("9999999999999")
                .title("Le Petit Prince")
                .authors(authorList)
                .summary("Un livre sur l'aviation, sur les moutons et les princes.")
                .pageCount(100)
                .build();

        bookDTOWithEmptyAuthorList = BookDTO.builder()
                .isbn("9999999999999")
                .title("Le Petit Prince")
                .summary("Un livre sur l'aviation, sur les moutons et les princes.")
                .authors(Collections.emptyList())
                .pageCount(100)
                .build();
    }

    @Test
    void shouldReturnListOfBooks() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.booksToBookDTOs(List.of(book))).thenReturn(List.of(bookDTO));

        // Act
        List<BookDTO> result = bookService.getAllBooks();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Le Petit Prince");
        assertThat(result.getFirst().getSummary()).isEqualTo("Un livre sur l'aviation, sur les moutons et les princes.");
        assertThat(result.getFirst().getPageCount()).isEqualTo(100);
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksExist() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        when(bookMapper.booksToBookDTOs(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<BookDTO> result = bookService.getAllBooks();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnBookById() {
        // Arrange
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);

        // Act
        BookDTO result = bookService.getBookByIsbn(book.getIsbn());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Le Petit Prince");
        assertThat(result.getSummary()).isEqualTo("Un livre sur l'aviation, sur les moutons et les princes.");
        assertThat(result.getPageCount()).isEqualTo(100);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenBookIdNotFound() {
        // Arrange
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.empty());

        // Act & Assert
        String isbnToThrow = book.getIsbn();
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookByIsbn(isbnToThrow));
        verify(bookRepository, times(1)).findByIsbn(book.getIsbn());
    }

    @Test
    void shouldSearchBooksByTitle() {
        // Arrange
        when(bookRepository.findByTitleContainingIgnoreCase("petit")).thenReturn(List.of(book));
        when(bookMapper.booksToBookDTOs(List.of(book))).thenReturn(List.of(bookDTO));

        // Act
        List<BookDTO> result = bookService.searchBooks("petit", null);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Le Petit Prince");
    }

    @Test
    void shouldReturnEmptyListWhenBookTitleDoesNotExist() {
        // Arrange
        when(bookRepository.findByTitleContainingIgnoreCase("grand")).thenReturn(Collections.emptyList());
        when(bookMapper.booksToBookDTOs(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<BookDTO> result = bookService.searchBooks("grand", null);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnAllBooksWhenBlankOrNoCriteria() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.booksToBookDTOs(List.of(book))).thenReturn(List.of(bookDTO));

        // Act
        List<BookDTO> result1 = bookService.searchBooks("", null);
        List<BookDTO> result2 = bookService.searchBooks(null, "");
        List<BookDTO> result3 = bookService.searchBooks(null, null);

        // Assert
        assertThat(result1).hasSize(1);
        assertThat(result2).hasSize(1);
        assertThat(result3).hasSize(1);
    }

    @Test
    void shouldSearchBooksByAuthor() {
        // Arrange
        when(bookRepository.findByAuthorsNameContainingIgnoreCase("saint-ex")).thenReturn(List.of(book));
        when(bookMapper.booksToBookDTOs(List.of(book))).thenReturn(List.of(bookDTO));

        // Act
        List<BookDTO> result = bookService.searchBooks(null, "saint-ex");

        // Assert
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListWhenBookAuthorDoesNotExist() {
        // Arrange
        when(bookRepository.findByAuthorsNameContainingIgnoreCase("unknown")).thenReturn(Collections.emptyList());
        when(bookMapper.booksToBookDTOs(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<BookDTO> result = bookService.searchBooks(null, "unknown");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void shouldCreateBookWithoutAuthor() {
        // Arrange
        when(bookMapper.bookDTOToBook(bookDTOWithEmptyAuthorList)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTOWithEmptyAuthorList);

        // Act
        BookDTO result = bookService.createBook(bookDTOWithEmptyAuthorList);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Le Petit Prince");
        assertThat(result.getSummary()).isEqualTo("Un livre sur l'aviation, sur les moutons et les princes.");
        assertThat(result.getPageCount()).isEqualTo(100);
        assertThat(result.getAuthors()).isEmpty();
    }

    @Test
    void shouldUpdateBookWithoutAuthor() {
        // Arrange
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(Optional.of(book));
        when(authorService.findOrCreateAuthor(author.getName())).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);

        // Act
        BookDTO result = bookService.updateBook(bookDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Le Petit Prince");
        assertThat(result.getPageCount()).isEqualTo(100);
        assertThat(result.getAuthors()).isNotNull().hasSize(1);
    }

    @Test
    void shouldUpdateBookWithEmptyAuthorList() {
        // Arrange
        when(bookRepository.findByIsbn(bookDTOWithEmptyAuthorList.getIsbn())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTOWithEmptyAuthorList);

        // Act
        BookDTO result = bookService.updateBook(bookDTOWithEmptyAuthorList);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Le Petit Prince");
        assertThat(result.getPageCount()).isEqualTo(100);
        assertThat(result.getAuthors()).isEmpty();
        verify(authorService, times(0)).findOrCreateAuthor(any(String.class));
    }



    @Test
    void shouldDeleteBook() {
        // Arrange
        when(bookRepository.existsByIsbn("12345")).thenReturn(true);

        // Act
        bookService.deleteBook("12345");

        // Assert
        verify(bookRepository, atMostOnce()).deleteByIsbn("12345");
    }

    @Test
    void shouldDeleteBookWithIdNotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook("9999999999"));

    }
}
