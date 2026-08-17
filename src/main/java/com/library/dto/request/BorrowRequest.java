package com.library.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRequest {

    @NotNull(message = "Member Id is required")
<<<<<<< HEAD
    @Schema(description = "Member ID", example = "1")
    private Long memberId;

    @NotNull(message = "Book Id is required")
    @Schema(description = "Book ID", example = "1")
=======
    @Schema(description = "Member ID", example = "M1001")
    private Long memberId;

    @NotNull(message = "Book Id is required")
    @Schema(description = "Book ID", example = "B1001")
>>>>>>> 508f3eae2851d7b50f65c2519dc5c38db4bd4a6d
    private Long bookId;
}