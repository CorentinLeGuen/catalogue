package com.librairie.catalogue.repository;

import com.librairie.catalogue.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
    void deleteByIsbn(String isbn);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAuthorsNameContainingIgnoreCase(String authorsName);
    List<Book> findByTitleContainingIgnoreCase(String title);
}
