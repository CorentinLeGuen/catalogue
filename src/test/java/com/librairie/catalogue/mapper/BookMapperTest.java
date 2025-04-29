package com.librairie.catalogue.mapper;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.librairie.catalogue.model.Author;

import static com.librairie.catalogue.mapper.BookMapper.mapAuthorsToNames;
import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {

    @Test
    void shouldMapAuthorNames() {
        // Given
        Author author1 = Author.builder().name("Author 1").build();
        Author author2 = Author.builder().name("Author 2").build();

        Set<Author> authors = new HashSet<>();
        authors.add(author1);
        authors.add(author2);

        // When
        List<String> result = mapAuthorsToNames(authors);

        // Then
        assertThat(result).isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder("Author 1", "Author 2");
    }

    @Test
    void shouldMapEmptyListOfEmptyAuthorSet() {
        // Given
        Set<Author> authors = new HashSet<>();

        // When
        List<String> result = mapAuthorsToNames(authors);

        // Then
        assertThat(result).isNotNull()
                .isEmpty();
    }

    @Test
    void shouldMapEmptyListOfNullAuthorSet() {
        // When
        List<String> result = mapAuthorsToNames(null);

        // Then
        assertThat(result).isNotNull()
                .isEmpty();
    }
}