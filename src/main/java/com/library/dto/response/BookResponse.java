// What client RECEIVES back:
package com.library.dto.response;


import com.library.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // getters + setters + toString + equals + hashCode
@NoArgsConstructor
@AllArgsConstructor

public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String genre;        // display name, not enum constant
    private int totalCopies;
    private int availableCopies;

}