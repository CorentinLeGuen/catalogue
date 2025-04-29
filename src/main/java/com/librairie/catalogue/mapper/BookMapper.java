package com.librairie.catalogue.mapper;

import com.librairie.catalogue.model.Author;
import com.librairie.catalogue.model.Book;
import com.librairie.catalogue.model.dto.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authors", source = "authors", qualifiedByName = "authorsToNames")
    BookDTO bookToBookDTO(Book book);

    @Mapping(target = "authors", ignore = true)
    Book bookDTOToBook(BookDTO bookDTO);

    List<BookDTO> booksToBookDTOs(List<Book> books);

    @Named("authorsToNames")
    static List<String> mapAuthorsToNames(Set<Author> authors) {
        if (authors == null) {
            return List.of();
        }
        return authors.stream()
                .map(Author::getName)
                .toList();
    }
}