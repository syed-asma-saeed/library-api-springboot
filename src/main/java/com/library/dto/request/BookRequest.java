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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // getters + setters + toString + equals + hashCode
@NoArgsConstructor
@AllArgsConstructor

public class BookRequest {
    private String title;
    private String author;
    private Genre genre;
    private int totalCopies;

}