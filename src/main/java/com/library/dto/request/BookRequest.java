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
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Book title", example = "Clean Code")
    private String title;

    @NotBlank(message = "Author is required")
    @Schema(description = "Book author", example = "Robert C. Martin")
    private String author;

    @NotNull(message = "Genre is required")
    @Schema(description = "Book genre", example = "TECHNOLOGY")
    private Genre genre;

    @Min(value = 1, message = "Must have at least 1 copy")
    @Schema(description = "Total number of copies", example = "3")
    private int totalCopies;

}