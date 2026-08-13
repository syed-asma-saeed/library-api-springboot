// What client RECEIVES back:
package com.library.dto.response;


import com.library.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Book response object")
@Data @NoArgsConstructor @AllArgsConstructor
public class BookResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Clean Code")
    private String title;

    @Schema(example = "Robert C. Martin")
    private String author;

    @Schema(example = "Technology")
    private String genre;

    @Schema(example = "3")
    private int totalCopies;

    @Schema(example = "2")
    private int availableCopies;
}