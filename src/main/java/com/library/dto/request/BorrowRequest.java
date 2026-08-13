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
    @Schema(description = "Member ID", example = "M1001")
    private Long memberId;

    @NotNull(message = "Book Id is required")
    @Schema(description = "Book ID", example = "B1001")
    private Long bookId;
}