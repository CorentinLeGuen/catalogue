package com.librairie.catalogue.mapper;

import com.librairie.catalogue.model.Author;
import com.librairie.catalogue.model.dto.AuthorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthorMapperImplTest {
    private AuthorMapper mapper;
    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        mapper = new AuthorMapperImpl();
        author = Author.builder().id(1L).name("John Doe").build();
        authorDTO = AuthorDTO.builder().name("John Doe").build();
    }

    @Test
    void shouldMapAuthorToAuthorDTO() {
        // Act
        AuthorDTO result = mapper.authorToAuthorDTO(author);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    void shouldMapNullAuthorToNullAuthorDTO() {
        // Act
        AuthorDTO result = mapper.authorToAuthorDTO(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void shouldMapAuthorDTOToAuthor() {
        // Act
        Author result = mapper.authorDTOToAuthor(authorDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    void shouldMapNullAuthorDTOToNullAuthor() {
        // Act
        Author result = mapper.authorDTOToAuthor(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void shouldMapListOfAuthorToListOfAuthorDTO() {
        // Arrange
        List<Author> authorsTest = new ArrayList<>();
        authorsTest.add(author);

        // Act
        List<AuthorDTO> result = mapper.authorsToAuthorDTOs(authorsTest);

        // Assert
        assertThat(result).isNotNull().isNotEmpty().hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("John Doe");
    }


    @Test
    void shouldMapEmptyListOfAuthorToEmptyListOfAuthorDTO() {
        // Act
        List<AuthorDTO> result = mapper.authorsToAuthorDTOs(new ArrayList<>());

        // Assert
        assertThat(result).isNotNull().isEmpty();

    }

    @Test
    void shouldMapNullAuthorListToNullAuthorDTOList() {
        // Act
        List<AuthorDTO> result = mapper.authorsToAuthorDTOs(null);

        // Assert
        assertThat(result).isNull();
    }

}
