package com.librairie.catalogue.service;

import com.librairie.catalogue.exception.ResourceNotFoundException;
import com.librairie.catalogue.mapper.AuthorMapper;
import com.librairie.catalogue.model.Author;
import com.librairie.catalogue.model.dto.AuthorDTO;
import com.librairie.catalogue.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.authorsToAuthorDTOs(authors);
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find author with id : " + id));
        return authorMapper.authorToAuthorDTO(author);
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = findOrCreateAuthor(authorDTO.getName());
        return authorMapper.authorToAuthorDTO(author);
    }

    Author findOrCreateAuthor(String name) {
        return authorRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> authorRepository.save(new Author(name)));
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find author with id : " + id));

        existingAuthor.setName(authorDTO.getName());

        Author updatedAuthor = authorRepository.save(existingAuthor);
        return authorMapper.authorToAuthorDTO(updatedAuthor);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
