package com.librairie.catalogue.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librairie.catalogue.model.dto.BookDTO;
import com.librairie.catalogue.repository.BookRepository;
import com.librairie.catalogue.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        Mockito.reset(bookService, bookRepository);
    }

    @Test
    void shouldReturnListOfBooks() throws Exception {
        // Arrange
        BookDTO book = BookDTO.builder()
                .isbn("1234567890123")
                .title("Le Petit Prince")
                .authors(List.of("Antoine de Saint-Exupéry"))
                .publicationDate(LocalDate.of(1943, 4, 6))
                .summary("Un court résumé")
                .pageCount(96)
                .build();

        when(bookService.getAllBooks()).thenReturn(List.of(book));

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Le Petit Prince"));
    }


    @Test
    void shouldCreateBook() throws Exception {
        BookDTO bookToCreate = BookDTO.builder()
                .isbn("1234567890123")
                .title("Le Petit Prince")
                .authors(List.of("Antoine de Saint-Exupéry"))
                .publicationDate(LocalDate.of(1943, 4, 6))
                .summary("Un court résumé")
                .pageCount(96)
                .build();

        when(bookService.createBook(bookToCreate)).thenReturn(bookToCreate);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Le Petit Prince"));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        // Arrange
        BookDTO book = BookDTO.builder()
                .isbn("1234567890123")
                .title("Le Petit Prince")
                .authors(List.of("Antoine de Saint-Exupéry"))
                .publicationDate(LocalDate.of(1943, 4, 6))
                .summary("Un court résumé")
                .pageCount(96)
                .build();

        when(bookService.getBookByIsbn("1234567890123")).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/api/books/{isbn}", "1234567890123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Le Petit Prince"))
                .andExpect(jsonPath("$.authors[0]").value("Antoine de Saint-Exupéry"));
    }

    @Test
    void shouldSearchBooksByTitle() throws Exception {
        // Arrange
        BookDTO book = BookDTO.builder()
                .isbn("1234567890123")
                .title("Nouveau Roman")
                .authors(List.of("Auteur Inconnu"))
                .publicationDate(LocalDate.of(2023, 2, 15))
                .summary("Résumé du livre.")
                .pageCount(250)
                .build();

        when(bookService.searchBooks("Nouveau", null)).thenReturn(List.of(book));

        // Act & Assert
        mockMvc.perform(get("/api/books/search")
                        .param("title", "Nouveau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Nouveau Roman"));
    }

    @Test
    void shouldSearchBooksByAuthor() throws Exception {
        // Arrange
        BookDTO book = BookDTO.builder()
                .isbn("1234567890123")
                .title("Un autre roman")
                .authors(List.of("Auteur Inconnu"))
                .publicationDate(LocalDate.of(2023, 3, 20))
                .summary("Résumé du livre.")
                .pageCount(300)
                .build();

        when(bookService.searchBooks(null, "Auteur")).thenReturn(List.of(book));

        // Act & Assert
        mockMvc.perform(get("/api/books/search")
                        .param("author", "Auteur"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].authors[0]").value("Auteur Inconnu"));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        // Arrange
        BookDTO updatedBook = BookDTO.builder()
                .isbn("1234567890123")
                .title("Livre Modifié")
                .authors(List.of("Auteur Modifié"))
                .publicationDate(LocalDate.of(2025, 1, 1))
                .summary("Un résumé mis à jour.")
                .pageCount(320)
                .build();

        when(bookService.updateBook(any(BookDTO.class))).thenReturn(updatedBook);

        // Act & Assert
        mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Livre Modifié"))
                .andExpect(jsonPath("$.authors[0]").value("Auteur Modifié"));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(delete("/api/books/{isbn}", "9999999999"))
                .andExpect(status().isOk());
        Mockito.verify(bookService).deleteBook("9999999999");
    }
}
