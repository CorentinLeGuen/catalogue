package com.librairie.catalogue.service;

import com.librairie.catalogue.exception.ResourceNotFoundException;
import com.librairie.catalogue.mapper.BookMapper;
import com.librairie.catalogue.model.Author;
import com.librairie.catalogue.model.Book;
import com.librairie.catalogue.model.dto.BookDTO;
import com.librairie.catalogue.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, AuthorService authorService, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> getAllBooks() {
        return bookMapper.booksToBookDTOs(bookRepository.findAll());
    }

    public BookDTO getBookByIsbn(String isbn) {
        return bookMapper.bookToBookDTO(bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find the book with ISBN: " + isbn)));
    }

    public List<BookDTO> searchBooks(String title, String author) {
        if (title != null && !title.isBlank()) {
            return bookMapper.booksToBookDTOs(bookRepository.findByTitleContainingIgnoreCase(title));
        }
        if (author != null && !author.isBlank()) {
            return bookMapper.booksToBookDTOs(bookRepository.findByAuthorsNameContainingIgnoreCase(author));
        }
        return bookMapper.booksToBookDTOs(bookRepository.findAll());
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookMapper.bookDTOToBook(bookDTO);
        if (bookDTO.getAuthors() != null) {
            Set<Author> authors = bookDTO.getAuthors().stream()
                    .map(authorService::findOrCreateAuthor)
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        } else {
            book.setAuthors(Collections.emptySet());
        }
        Book createdBook = bookRepository.save(book);
        return bookMapper.bookToBookDTO(createdBook);
    }

    public BookDTO updateBook(BookDTO bookDTO) {
        Book existingBook = bookRepository.findByIsbn(bookDTO.getIsbn())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ISBN: " + bookDTO.getIsbn()));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setPageCount(bookDTO.getPageCount());
        existingBook.setSummary(bookDTO.getSummary());
        existingBook.setPublicationDate(bookDTO.getPublicationDate());

        if (bookDTO.getAuthors() != null) {
            Set<Author> authors = bookDTO.getAuthors().stream()
                    .map(authorService::findOrCreateAuthor)
                    .collect(Collectors.toSet());
            existingBook.setAuthors(authors);
        } else {
            existingBook.setAuthors(Collections.emptySet());
        }

        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.bookToBookDTO(updatedBook);
    }

    public void deleteBook(String isbn) {
        if (!bookRepository.existsByIsbn(isbn)) {
            throw new ResourceNotFoundException("Book not found with ISBN: " + isbn);
        }
        bookRepository.deleteByIsbn(isbn);
    }

}
