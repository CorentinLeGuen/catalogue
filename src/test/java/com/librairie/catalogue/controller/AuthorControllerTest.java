package com.librairie.catalogue.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librairie.catalogue.exception.ResourceNotFoundException;
import com.librairie.catalogue.model.dto.AuthorDTO;
import com.librairie.catalogue.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthorController.class)
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthorDTO author;

    @BeforeEach
    void setUp() {
        author = AuthorDTO.builder()
                .name("Victor Hugo")
                .build();
    }

    @Test
    void shouldReturnAllAuthors() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(List.of(author));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Victor Hugo"));
    }

    @Test
    void shouldReturnEmptyAuthorsIfNoAuthors() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(List.of());

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnAuthorById() throws Exception {
        when(authorService.getAuthorById(1L)).thenReturn(author);

        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Victor Hugo"));
    }

    @Test
    void shouldReturn200IfAuthorNull() throws Exception {
        when(authorService.getAuthorById(50L)).thenReturn(null);

        mockMvc.perform(get("/api/authors/50"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void shouldReturn404IfAuthorNotFound() throws Exception {
        when(authorService.getAuthorById(50L)).thenThrow(new ResourceNotFoundException("Author not found"));

        mockMvc.perform(get("/api/authors/50"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Author not found"));
    }

    @Test
    void shouldCreateAuthor() throws Exception {
        when(authorService.createAuthor(any(AuthorDTO.class))).thenReturn(author);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Victor Hugo"));
    }

    @Test
    void shouldUpdateAuthor() throws Exception {
        when(authorService.updateAuthor(eq(1L), any(AuthorDTO.class))).thenReturn(author);

        mockMvc.perform(put("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Victor Hugo"));
    }

    @Test
    void shouldDeleteAuthor() throws Exception {
        doNothing().when(authorService).deleteAuthor(1L);

        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isOk());

        verify(authorService).deleteAuthor(1L);
    }
}