package com.librairie.catalogue.model.dto;

import com.librairie.catalogue.validation.UniqueISBN;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    @NotBlank(message = "ISBN is mandatory")
    @Pattern(
            regexp = "^\\w{10}(\\w{3})?$",
            message = "ISBN format should be ISBN-10 or ISBN-13 format"
    )
    @UniqueISBN
    private String isbn;
    private String title;
    private List<String> authors;
    private LocalDate publicationDate;
    private String summary;
    private int pageCount;
}
