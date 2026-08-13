package com.library.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowResponse {
    @Schema(example = "1") private Long recordId;
    @Schema(example = "Rahul Sharma") private String memberName;
    @Schema(example = "Clean Code") private String bookTitle;
    @Schema(example = "2024-01-15") private LocalDate borrowDate;
    @Schema(example = "2024-01-29") private LocalDate dueDate;
    @Schema(example = "false") private boolean returned;
    @Schema(example = "0.0") private double fine;
}