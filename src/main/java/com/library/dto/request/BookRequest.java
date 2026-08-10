/*
Never expose your @Entity directly through the API.

@Entity has JPA annotations, lazy loading proxies,
circular references (Book → BorrowRecord → Book → ...)
All of these break JSON serialization or expose internal details.
DTOs are clean, controlled, what the client actually needs.

 */

// What client SENDS when creating a book:
package com.library.dto.request;

import com.library.enums.Genre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // getters + setters + toString + equals + hashCode
@NoArgsConstructor
@AllArgsConstructor

public class BookRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotNull(message = "Genre is required")
    private Genre genre;

    @Min(value = 1, message = "Must have at least 1 copy")
    private int totalCopies;

}