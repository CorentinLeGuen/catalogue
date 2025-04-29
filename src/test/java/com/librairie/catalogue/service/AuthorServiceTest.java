package com.librairie.catalogue.service;

import com.librairie.catalogue.exception.ResourceNotFoundException;
import com.librairie.catalogue.mapper.AuthorMapper;
import com.librairie.catalogue.model.Author;
import com.librairie.catalogue.model.dto.AuthorDTO;
import com.librairie.catalogue.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .id(1L)
                .name("Antoine de Saint-Exupéry")
                .build();

        authorDTO = AuthorDTO.builder()
                .name("Antoine de Saint-Exupéry")
                .build();
    }

    @Test
    void shouldReturnAllAuthors() {
        // Arrange
        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(authorMapper.authorsToAuthorDTOs(List.of(author))).thenReturn(List.of(authorDTO));

        // Act
        List<AuthorDTO> authors = authorService.getAllAuthors();

        // Assert
        assertThat(authors).hasSize(1);
        assertThat(authors.getFirst().getName()).isEqualTo(author.getName());
    }

    @Test
    void shouldReturnAuthorById() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDTO(author)).thenReturn(authorDTO);

        // Act
        AuthorDTO result = authorService.getAuthorById(1L);

        // Assert
        assertThat(result.getName()).isEqualTo(author.getName());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAuthorIdNotFound() {
        // Arrange
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorById(999L));

        verify(authorRepository, atMostOnce()).findById(999L);
    }

    @Test
    void shouldCreateAuthor() {
        // Arrange
        when(authorRepository.findByNameIgnoreCase("Antoine de Saint-Exupéry"))
                .thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDTO(author)).thenReturn(authorDTO);

        // Act
        AuthorDTO result = authorService.createAuthor(authorDTO);

        // Assert
        assertThat(result.getName()).isEqualTo(author.getName());
        verify(authorRepository, atMostOnce()).save(author);
    }

    @Test
    void shouldFindExistingAuthor() {
        // Arrange
        when(authorRepository.findByNameIgnoreCase("Antoine de Saint-Exupéry"))
                .thenReturn(Optional.of(author));

        // Act
        Author result = authorService.findOrCreateAuthor("Antoine de Saint-Exupéry");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Antoine de Saint-Exupéry");
        verify(authorRepository, never()).save(any());
    }

    @Test
    void shouldCreateNewAuthorWhenNotFound() {
        // Arrange
        when(authorRepository.findByNameIgnoreCase("Albert Camus"))
                .thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Author result = authorService.findOrCreateAuthor("Albert Camus");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Albert Camus");
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void shouldUpdateExistingAuthor() {
        // Arrange
        Author updatedAuthor = Author.builder()
                        .name("updated")
                                .build();
        AuthorDTO updatedAuthorDTO = AuthorDTO.builder()
                        .name("updated")
                                .build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(updatedAuthor);
        when(authorMapper.authorToAuthorDTO(updatedAuthor)).thenReturn(updatedAuthorDTO);

        // Act
        AuthorDTO result = authorService.updateAuthor(1L, updatedAuthorDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(updatedAuthor.getName());
        verify(authorRepository, atMostOnce()).findById(1L);
        verify(authorRepository, atMostOnce()).save(updatedAuthor);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingAuthorIdNotFound() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> authorService.updateAuthor(1L, authorDTO));
        verify(authorRepository, atMostOnce()).findById(1L);
        verify(authorRepository, never()).save(any());
    }

    @Test
    void shouldDeleteAuthor() {
        // Act
        authorService.deleteAuthor(1L);

        // Assert
        verify(authorRepository, atMostOnce()).deleteById(1L);
    }

    @Test
    void shouldDeleteAuthorWithIdNotFound() {
        // Act
        authorService.deleteAuthor(999L);

        // Assert
        verify(authorRepository, atMostOnce()).deleteById(999L);
    }
}
