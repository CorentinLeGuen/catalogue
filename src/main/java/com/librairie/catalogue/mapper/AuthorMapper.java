package com.librairie.catalogue.mapper;

import com.librairie.catalogue.model.Author;
import com.librairie.catalogue.model.dto.AuthorDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDTO authorToAuthorDTO(Author author);
    Author authorDTOToAuthor(AuthorDTO authorDTO);
    List<AuthorDTO> authorsToAuthorDTOs(List<Author> authors);
}