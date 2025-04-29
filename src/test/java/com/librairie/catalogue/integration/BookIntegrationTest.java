package com.librairie.catalogue.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librairie.catalogue.model.dto.BookDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String bookIsbn;

    @Test
    @Order(1)
    void shouldCreateBook() throws Exception {
        bookIsbn = "9999999999";

        BookDTO book = BookDTO.builder()
                .isbn(bookIsbn)
                .title("Functional Test Book")
                .authors(List.of("Author"))
                .publicationDate(LocalDate.of(2024, 1, 1))
                .summary("Functional test book creation")
                .pageCount(300)
                .build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(bookIsbn))
                .andExpect(jsonPath("$.title").value("Functional Test Book"));
    }

    @Test
    @Order(2)
    void shouldGetBookByIsbn() throws Exception {
        mockMvc.perform(get("/api/books/" + bookIsbn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(bookIsbn));
    }

    @Test
    @Order(3)
    void shouldSearchBookByTitle() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("title", "Functional Test Book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value(bookIsbn));
    }

    @Test
    @Order(4)
    void shouldUpdateBook() throws Exception {
        BookDTO updatedBook = BookDTO.builder()
                .isbn(bookIsbn)
                .title("Updated Title")
                .authors(List.of("Updated Author"))
                .publicationDate(LocalDate.of(2025, 2, 2))
                .summary("Updated summary")
                .pageCount(400)
                .build();

        mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @Order(5)
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/" + bookIsbn))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void shouldReturnNotFoundAfterDeletion() throws Exception {
        mockMvc.perform(get("/api/books/" + bookIsbn))
                .andExpect(status().isNotFound());
    }
}
