package com.librairie.catalogue.mapper;

import com.librairie.catalogue.model.Author;
import com.librairie.catalogue.model.Book;
import com.librairie.catalogue.model.dto.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class BookMapperImplTest {

    private Book book;
    private BookDTO bookDTO;
    private LocalDate date;
    private Author author1;
    private Author author2;

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapperImpl();

        author1 = Author.builder().name("author1").build();
        author2 = Author.builder().name("author2").build();

        date = LocalDate.ofEpochDay(50L);
        Set<Author> authorsSet = new HashSet<>(2);
        authorsSet.add(author1);
        authorsSet.add(author2);
        List<String> authorsList = new ArrayList<>(2);
        authorsList.add(author1.getName());
        authorsList.add(author2.getName());

        book = Book.builder()
                .id(1L)
                .authors(authorsSet)
                .isbn("ISBN")
                .title("Title")
                .publicationDate(date)
                .summary("Summary")
                .pageCount(20)
                .build();

        bookDTO = BookDTO.builder()
                .authors(authorsList)
                .isbn("ISBN")
                .title("Title")
                .publicationDate(date)
                .summary("Summary")
                .pageCount(20)
                .build();
    }

    @Test
    void shouldMapBookToBookDTO() {
        // Act
        BookDTO result = bookMapper.bookToBookDTO(book);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAuthors()).hasSize(2);
        assertThat(result.getAuthors()).containsExactlyInAnyOrder(author1.getName(), author2.getName());
        assertThat(result.getIsbn()).isEqualTo("ISBN");
        assertThat(result.getTitle()).isEqualTo("Title");
        assertThat(result.getPublicationDate()).isEqualTo(date);
        assertThat(result.getSummary()).isEqualTo("Summary");
        assertThat(result.getPageCount()).isEqualTo(20);
    }

    @Test
    void shouldMapNullBookToNullBookDTO() {
        // Act
        BookDTO result = bookMapper.bookToBookDTO(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void shouldMapBookDTOToBook() {
        // Act
        Book result = bookMapper.bookDTOToBook(bookDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAuthors()).isEmpty();
        assertThat(result.getIsbn()).isEqualTo("ISBN");
        assertThat(result.getTitle()).isEqualTo("Title");
        assertThat(result.getPublicationDate()).isEqualTo(date);
        assertThat(result.getSummary()).isEqualTo("Summary");
        assertThat(result.getPageCount()).isEqualTo(20);
    }

    @Test
    void shouldMapNullBookDTOToNullBook() {
        // Act
        Book result = bookMapper.bookDTOToBook(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void shouldMapListOfBookToListOfBookDTO() {
        // Arrange
        List<Book> books = List.of(book);

        // Act
        List<BookDTO> result = bookMapper.booksToBookDTOs(books);

        // Assert
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.getFirst()).isNotNull();
        assertThat(result.getFirst().getAuthors()).hasSize(2);
        assertThat(result.getFirst().getAuthors()).containsExactlyInAnyOrder(author1.getName(), author2.getName());
        assertThat(result.getFirst().getIsbn()).isEqualTo("ISBN");
        assertThat(result.getFirst().getTitle()).isEqualTo("Title");
        assertThat(result.getFirst().getPublicationDate()).isEqualTo(date);
        assertThat(result.getFirst().getSummary()).isEqualTo("Summary");
        assertThat(result.getFirst().getPageCount()).isEqualTo(20);
    }

    @Test
    void shouldMapEmptyListOfBookToEmptyListOfBookDTO() {
        // Arrange
        List<Book> books = List.of();

        // Act
        List<BookDTO> result = bookMapper.booksToBookDTOs(books);

        // Assert
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void shouldMapNullListOfBookToNullListOfBookDTO() {
        // Act
        List<BookDTO> result = bookMapper.booksToBookDTOs(null);

        // Assert
        assertThat(result).isNull();
    }
}
