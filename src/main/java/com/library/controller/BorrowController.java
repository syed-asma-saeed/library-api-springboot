package com.library.controller;

import com.library.dto.request.BorrowRequest;
import com.library.dto.request.MemberRequest;
import com.library.dto.response.BorrowResponse;
import com.library.dto.response.MemberResponse;
import com.library.dto.response.PageResponse;
import com.library.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Borrow Operations", description = "Endpoints for borrowing books, returning them, and viewing history")
@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService){
        this.borrowService = borrowService;
    }

    @PostMapping
    @Operation(summary = "Borrow a Book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book borrowed successfully"),
            @ApiResponse(responseCode = "400", description = "No copies available OR Borrow Limit exceeded"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {  //@valid works only one @RequestBody
        return ResponseEntity.status(201).body(borrowService.borrowBook(request));
    }

    @PostMapping("return/{recordId}")
    @Operation(summary = "Return a Book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "400", description = "Book already returned"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long recordId){
        return ResponseEntity.ok(borrowService.returnBook(recordId));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue records")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Overdue records retrieved (may be empty list)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required")
    })
    public ResponseEntity<PageResponse<BorrowResponse>> getOverdueRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(
                borrowService.getOverdueRecords(page, size, sortBy, sortDir)
        );
    }

    @GetMapping("/history/{memberId}")
    @Operation(summary = "Get member borrow history")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Borrow history retrieved (may be empty list)"),
            @ApiResponse(responseCode = "401", description = "No history available"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<PageResponse<BorrowResponse>> getMemberHistory(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                borrowService.getMemberHistory(memberId, page, size)
        );
    }

}