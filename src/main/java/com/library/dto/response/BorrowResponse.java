package com.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowResponse {
    private Long recordId;
    private String memberName;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;
    private double fine;
}