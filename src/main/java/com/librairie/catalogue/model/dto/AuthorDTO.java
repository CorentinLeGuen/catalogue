package com.librairie.catalogue.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    @NotBlank(message = "Author name is mandatory")
    @Size(min = 2, message = "Author name should contains 2 characters at least")
    private String name;
}
